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
      <wsin:CreateContractV4>
         <wsin:ClientSearchMethod>CLIENT_ID</wsin:ClientSearchMethod>
         <wsin:ClientIdentifier>96100</wsin:ClientIdentifier>
         <wsin:Reason>Create Terminal for Acquirer Test</wsin:Reason>
         <wsin:CreateContract_InObject>
            <wsin:Branch>0101</wsin:Branch>
            <wsin:InstitutionCode>0001</wsin:InstitutionCode>
            <wsin:ProductCode>RETAIL_TERMINAL</wsin:ProductCode>
            <wsin:ProductCode2></wsin:ProductCode2>
            <wsin:ProductCode3></wsin:ProductCode3>
            <wsin:ContractName>Retail POS Terminal 01</wsin:ContractName>
            <wsin:CBSNumber>21324556600</wsin:CBSNumber>   
            {extra_tag}
         </wsin:CreateContract_InObject>
      </wsin:CreateContractV4>
   </soapenv:Body>
</soapenv:Envelope>
"""

tags = [
    "<wsin:ParentContractNumber>001-C-508109</wsin:ParentContractNumber>",
    "<wsin:ParentContract>001-C-508109</wsin:ParentContract>",
    "<wsin:ParentContractIdentifier>001-C-508109</wsin:ParentContractIdentifier>",
    "<wsin:LiabContractNumber>001-C-508109</wsin:LiabContractNumber>",
    "<wsin:LiabContract>001-C-508109</wsin:LiabContract>",
    "<wsin:LiabContractIdentifier>001-C-508109</wsin:LiabContractIdentifier>",
    "<wsin:BillingContractNumber>001-C-508109</wsin:BillingContractNumber>",
    "<wsin:BillingContract>001-C-508109</wsin:BillingContract>"
]

def run_tests():
    for tag in tags:
        print(f"Testing parent tag: {tag}")
        xml_data = build_xml(tag)
        
        with open("temp_parent_test.xml", "w", encoding="utf-8") as f:
            f.write(xml_data)
            
        cmd = [
            "curl.exe", "-s",
            "--location", URL,
            "--header", "Content-Type: application/xml",
            "--data", "@temp_parent_test.xml"
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
