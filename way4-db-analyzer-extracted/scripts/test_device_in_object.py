import subprocess
import time

URL = "http://10.145.48.222:17000/webservice_int/ws"

def build_xml(extra_tag):
    return f"""<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wsin="http://www.openwaygroup.com/wsint">
   <soapenv:Header>
      <wsin:SessionContextStr></wsin:SessionContextStr>
      <wsin:UserInfo>officer="WX_ADMIN"</wsin:UserInfo>
      <wsin:CorrelationId></wsin:CorrelationId>
   </soapenv:Header>
   <soapenv:Body>
      <wsin:CreateIssuingContractWithLiabilityV2>
         <wsin:LiabCategory>Y</wsin:LiabCategory>
         <wsin:LiabContractSearchMethod>CONTRACT_NUMBER</wsin:LiabContractSearchMethod>
         <wsin:LiabContractIdentifier>001-C-259707</wsin:LiabContractIdentifier>
         <wsin:ClientSearchMethod>CLIENT_ID</wsin:ClientSearchMethod>
         <wsin:ClientIdentifier>96330</wsin:ClientIdentifier>
         <wsin:ProductCode>RETAIL_TERMINAL</wsin:ProductCode>
         <wsin:ProductCode2></wsin:ProductCode2>
         <wsin:ProductCode3></wsin:ProductCode3>
         <wsin:InObject>
            <wsin:Branch>0101</wsin:Branch>
            <wsin:InstitutionCode>0001</wsin:InstitutionCode>
            <wsin:ContractName>Merchant Outlet Contract</wsin:ContractName>
            <wsin:CBSNumber>213245566000</wsin:CBSNumber>
            {extra_tag}
         </wsin:InObject>  
      </wsin:CreateIssuingContractWithLiabilityV2>
   </soapenv:Body>
</soapenv:Envelope>
"""

tags = [
    "<wsin:DeviceType>STD</wsin:DeviceType>",
    "<wsin:DeviceTypeCode>STD</wsin:DeviceTypeCode>",
    "<wsin:DeviceType>33</wsin:DeviceType>",
    "<wsin:DeviceTypeCode>33</wsin:DeviceTypeCode>",
    "<wsin:DeviceTypeID>33</wsin:DeviceTypeID>",
    "<wsin:DeviceTypeId>33</wsin:DeviceTypeId>",
    "<wsin:DeviceTypeID>STD</wsin:DeviceTypeID>",
    "<wsin:DeviceTypeId>STD</wsin:DeviceTypeId>",
]

def run_tests():
    for tag in tags:
        print(f"Testing InObject tag: {tag}")
        xml_data = build_xml(tag)
        
        with open("temp_device_test.xml", "w", encoding="utf-8") as f:
            f.write(xml_data)
            
        cmd = [
            "curl.exe", "-s",
            "--location", URL,
            "--header", "Content-Type: application/xml",
            "--data", "@temp_device_test.xml"
        ]
        
        try:
            res = subprocess.run(cmd, capture_output=True, text=True, timeout=10)
            output = res.stdout
            if "Fault" in output:
                print("  Failed: SOAP Fault (Schema Mismatch)")
            else:
                print("  --> SCHEMA VALIDATED SUCCESS! (No SOAP Fault)")
                start = output.find("<RetMsg>")
                end = output.find("</RetMsg>")
                if start != -1 and end != -1:
                    print(f"  Response RetMsg: {output[start+8:end]}")
                else:
                    print("  No RetMsg element found, but valid XML")
        except Exception as e:
            print(f"  Error running: {e}")
            
        time.sleep(0.5)

if __name__ == "__main__":
    run_tests()
