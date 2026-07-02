package com.payment.service.service;

import com.payment.service.dto.request.DocSearchRequest;
import com.payment.service.dto.response.DocResponse;
import com.payment.service.dto.response.ContractCashFlowResponse;
import com.payment.service.entity.Currency;
import com.payment.service.entity.Doc;
import com.payment.service.entity.TransactionType;
import com.payment.service.repository.CurrencyRepository;
import com.payment.service.repository.DocRepository;
import com.payment.service.repository.TransactionTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DocService {

    private static final Set<String> ALLOWED_SORT_PROPERTIES = Set.of(
            "id", "transDate", "recDate", "postingDate", "transAmount", "returnCode");
    private static final String DEFAULT_SORT = "transDate,desc";
    private static final String DEFAULT_SORT_PROPERTY = "transDate";
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    DocRepository docRepository;
    CurrencyRepository currencyRepository;
    TransactionTypeRepository transactionTypeRepository;

    public Page<DocResponse> getDocsByContract(Long contractId, Integer page, Integer size) {
        Pageable pageable = buildPageable(page, size, DEFAULT_SORT);
        log.info("Fetching transaction history for contractId: {}", contractId);
        Page<Doc> docs = docRepository.findByContractId(contractId, pageable);
        return mapToResponsePage(docs);
    }

    public List<ContractCashFlowResponse> getContractCashFlows() {
        Map<String, String> currencyMap = currencyRepository.findAll().stream()
                .filter(currency -> currency.getNumericCode() != null)
                .collect(Collectors.toMap(Currency::getNumericCode, Currency::getCode, (first, ignored) -> first));
        return docRepository.summarizePostedCashFlowByContract().stream()
                .map(row -> ContractCashFlowResponse.builder()
                        .contractId(row.getContractId())
                        .contractNumber(maskUnlessAllowed(row.getContractNumber(), canViewSensitiveData()))
                        .contractName(row.getContractName())
                        .product(row.getProduct())
                        .contractLevel(row.getContractLevel())
                        .balanceCurrency(resolveCurrency(row.getBalanceCurrency(), currencyMap))
                        .currency(resolveCurrency(row.getCurrency(), currencyMap))
                        .amountAvailable(row.getAmountAvailable())
                        .totalBalance(row.getTotalBalance())
                        .totalBlocked(row.getTotalBlocked())
                        .totalReceived(defaultZero(row.getTotalReceived()))
                        .totalSent(defaultZero(row.getTotalSent()))
                        .netCashFlow(defaultZero(row.getTotalReceived()).subtract(defaultZero(row.getTotalSent())))
                        .transactionCount(row.getTransactionCount())
                        .firstTransactionDate(row.getFirstTransactionDate())
                        .lastTransactionDate(row.getLastTransactionDate())
                        .build())
                .toList();
    }

    public Page<DocResponse> searchDocs(DocSearchRequest request) {
        Pageable pageable = buildPageable(request.getPage(), request.getSize(), request.getSort());
        log.info("Searching transaction history. contractId: {}, number: {}, startDate: {}, endDate: {}, page: {}, size: {}, sort: {}",
                request.getContractId(), request.getNumber(), request.getStartDate(), request.getEndDate(),
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return searchDocs(
                request.getContractId(),
                request.getNumber(),
                request.getStartDate(),
                request.getEndDate(),
                request.getTransType(),
                request.getPostingStatus(),
                request.getDocumentCategory(),
                request.getRequestCategory(),
                request.getServiceClass(),
                request.getReturnCode(),
                pageable);
    }

    public Page<DocResponse> searchDocs(Long contractId, String number, Date startDate, Date endDate,
                                        Long transType, String postingStatus, String documentCategory,
                                        String requestCategory, String serviceClass, Integer returnCode,
                                        Pageable pageable) {
        log.info("Searching transaction history. contractId: {}, number: {}, startDate: {}, endDate: {}",
                contractId, number, startDate, endDate);
        Page<Doc> docs = docRepository.searchDocs(contractId, blankToNull(number), startDate, endDate,
                transType, blankToNull(postingStatus), blankToNull(documentCategory),
                blankToNull(requestCategory), blankToNull(serviceClass), returnCode, pageable);
        return mapToResponsePage(docs);
    }

    private Page<DocResponse> mapToResponsePage(Page<Doc> docs) {
        Map<String, String> currencyMap = new HashMap<>();
        try {
            currencyMap = currencyRepository.findAll().stream()
                    .filter(c -> c.getNumericCode() != null)
                    .collect(Collectors.toMap(
                            Currency::getNumericCode,
                            Currency::getCode,
                            (existing, replacement) -> existing
                    ));
        } catch (Exception e) {
            log.error("Failed to load currency lookup map", e);
        }

        final Map<String, String> finalCurrencyMap = currencyMap;

        Set<Long> typeIds = docs.getContent().stream()
                .map(Doc::getTransType)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<Long, TransactionType> transactionTypes = transactionTypeRepository.findAllById(typeIds).stream()
                .filter(type -> "A".equals(type.getAmndState()))
                .collect(Collectors.toMap(TransactionType::getId, type -> type));
        boolean canViewSensitiveData = canViewSensitiveData();

        return docs.map(doc -> {
            TransactionType type = transactionTypes.get(doc.getTransType());
            return DocResponse.builder()
                    .id(doc.getId())
                    .sourceNumber(maskUnlessAllowed(doc.getSourceNumber(), canViewSensitiveData))
                    .targetNumber(maskUnlessAllowed(doc.getTargetNumber(), canViewSensitiveData))
                    .transAmount(doc.getTransAmount())
                    .transCurr(doc.getTransCurr())
                    .transCurrName(doc.getTransCurr() != null ? finalCurrencyMap.getOrDefault(doc.getTransCurr(), doc.getTransCurr()) : null)
                    .transDate(doc.getTransDate())
                    .transDetails(doc.getTransDetails())
                    .authCode(maskUnlessAllowed(doc.getAuthCode(), canViewSensitiveData))
                    .postingStatus(doc.getPostingStatus())
                    .sourceContract(doc.getSourceContract())
                    .targetContract(doc.getTargetContract())
                    .transType(doc.getTransType())
                    .transTypeName(type != null ? type.getName() : null)
                    .transTypeIdt(type != null ? type.getIdentityCode() : null)
                    .debitCredit(type != null ? type.getDebitCredit() : null)
                    .addInfo(doc.getAddInfo())
                    .commentText(doc.getCommentText())
                    .reasonDetails(doc.getReasonDetails())
                    .returnCode(doc.getReturnCode())
                    .amndState(doc.getAmndState())
                    .amndDate(doc.getAmndDate())
                    .amndOfficer(doc.getAmndOfficer())
                    .amndPrev(doc.getAmndPrev())
                    .docOrigId(doc.getDocOrigId())
                    .docPrevId(doc.getDocPrevId())
                    .docSummId(doc.getDocSummId())
                    .docChainId(doc.getDocChainId())
                    .numberOfSubS(doc.getNumberOfSubS())
                    .numberInChain(doc.getNumberInChain())
                    .action(doc.getAction())
                    .messageCategory(doc.getMessageCategory())
                    .sourceRegNum(doc.getSourceRegNum())
                    .acqRefNumber(doc.getAcqRefNumber())
                    .retRefNumber(doc.getRetRefNumber())
                    .issRefNumber(doc.getIssRefNumber())
                    .psRefNumber(doc.getPsRefNumber())
                    .nwRefDate(doc.getNwRefDate())
                    .isAuthorization(doc.getIsAuthorization())
                    .requestCategory(doc.getRequestCategory())
                    .serviceClass(doc.getServiceClass())
                    .sourceCode(doc.getSourceCode())
                    .sourceFeeCode(doc.getSourceFeeCode())
                    .targetCode(doc.getTargetCode())
                    .targetFeeCode(doc.getTargetFeeCode())
                    .sourceChannel(doc.getSourceChannel())
                    .sCat(doc.getSCat())
                    .sourceIdtScheme(doc.getSourceIdtScheme())
                    .sourceMemberId(doc.getSourceMemberId())
                    .recMemberId(doc.getRecMemberId())
                    .sourceSpc(doc.getSourceSpc())
                    .sourceAccType(doc.getSourceAccType())
                    .sourceService(doc.getSourceService())
                    .targetChannel(doc.getTargetChannel())
                    .tCat(doc.getTCat())
                    .targetIdtScheme(doc.getTargetIdtScheme())
                    .targetMemberId(doc.getTargetMemberId())
                    .sendMemberId(doc.getSendMemberId())
                    .sendingBin(doc.getSendingBin())
                    .targetBinId(doc.getTargetBinId())
                    .targetSpc(doc.getTargetSpc())
                    .targetAccType(doc.getTargetAccType())
                    .targetService(doc.getTargetService())
                    .targetCountry(doc.getTargetCountry())
                    .cardExpire(doc.getCardExpire())
                    .cardSeqvNumber(doc.getCardSeqvNumber())
                    .merchantId(maskUnlessAllowed(doc.getMerchantId(), canViewSensitiveData))
                    .sicCode(doc.getSicCode())
                    .transCondition(doc.getTransCondition())
                    .transCondAttr(doc.getTransCondAttr())
                    .secTransCondAtt(doc.getSecTransCondAtt())
                    .reconsCurr(doc.getReconsCurr() != null ? finalCurrencyMap.getOrDefault(doc.getReconsCurr(), doc.getReconsCurr()) : null)
                    .reconsAmount(doc.getReconsAmount())
                    .settlCurr(doc.getSettlCurr() != null ? finalCurrencyMap.getOrDefault(doc.getSettlCurr(), doc.getSettlCurr()) : null)
                    .settlAmount(doc.getSettlAmount())
                    .sourceFeeCurr(doc.getSourceFeeCurr() != null ? finalCurrencyMap.getOrDefault(doc.getSourceFeeCurr(), doc.getSourceFeeCurr()) : null)
                    .sourceFeeAmount(doc.getSourceFeeAmount())
                    .targetFeeCurr(doc.getTargetFeeCurr() != null ? finalCurrencyMap.getOrDefault(doc.getTargetFeeCurr(), doc.getTargetFeeCurr()) : null)
                    .targetFeeAmount(doc.getTargetFeeAmount())
                    .secTransDate(doc.getSecTransDate())
                    .transCountry(doc.getTransCountry())
                    .transState(doc.getTransState())
                    .transCity(doc.getTransCity())
                    .binRecord(doc.getBinRecord())
                    .reasonCode(doc.getReasonCode())
                    .requirement(doc.getRequirement())
                    .postingDate(doc.getPostingDate())
                    .fxSettlDate(doc.getFxSettlDate())
                    .recDate(doc.getRecDate())
                    .outwardStatus(doc.getOutwardStatus())
                    .changeVersion(doc.getChangeVersion())
                    .partitionKey(doc.getPartitionKey())
                    .synchTag(doc.getSynchTag())
                    .build();
        });
    }

    public Map<String, Object> getFilterMetadata() {
        List<Long> usedTypeIds = docRepository.findUsedTransactionTypeIds();
        List<Map<String, Object>> transactionTypes = transactionTypeRepository.findAllById(usedTypeIds).stream()
                .filter(type -> "A".equals(type.getAmndState()))
                .sorted(Comparator.comparing(TransactionType::getName, Comparator.nullsLast(String::compareToIgnoreCase)))
                .map(type -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("id", type.getId());
                    item.put("name", type.getName());
                    item.put("identityCode", type.getIdentityCode());
                    return item;
                })
                .toList();

        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("transactionTypes", transactionTypes);
        metadata.put("postingStatuses", observedValues(docRepository.findUsedPostingStatuses()));
        metadata.put("documentCategories", observedValues(docRepository.findUsedDocumentCategories()));
        metadata.put("requestCategories", observedValues(docRepository.findUsedRequestCategories()));
        metadata.put("serviceClasses", observedValues(docRepository.findUsedServiceClasses()));
        return metadata;
    }

    private List<String> observedValues(List<String> observedCodes) {
        return observedCodes.stream().sorted().toList();
    }

    private Pageable buildPageable(Integer page, Integer size, String sort) {
        int resolvedPage = page == null || page < 0 ? DEFAULT_PAGE : page;
        int resolvedSize = size == null || size <= 0 ? DEFAULT_SIZE : size;
        String resolvedSort = blankToNull(sort);
        String[] sortParts = (resolvedSort == null ? DEFAULT_SORT : resolvedSort).split(",");
        String property = ALLOWED_SORT_PROPERTIES.contains(sortParts[0]) ? sortParts[0] : DEFAULT_SORT_PROPERTY;
        Sort.Direction direction = sortParts.length > 1 && "asc".equalsIgnoreCase(sortParts[1])
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return PageRequest.of(resolvedPage, resolvedSize, Sort.by(direction, property));
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private java.math.BigDecimal defaultZero(java.math.BigDecimal value) {
        return value == null ? java.math.BigDecimal.ZERO : value;
    }

    private String resolveCurrency(String value, Map<String, String> currencyMap) {
        return value == null ? null : currencyMap.getOrDefault(value, value);
    }

    private boolean canViewSensitiveData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority())
                        || "ROLE_SUPERVISOR".equals(authority.getAuthority()));
    }

    private String maskUnlessAllowed(String value, boolean allowed) {
        if (allowed || value == null || value.isBlank()) return value;
        if (value.length() <= 4) return "****";
        if (value.length() <= 8) return value.substring(0, 2) + "***" + value.substring(value.length() - 2);
        return value.substring(0, 4) + "***" + value.substring(value.length() - 4);
    }
}
