import subprocess
import time

URL = "http://10.145.48.222:17000/webservice_int/ws"

def build_xml(add_info):
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
         <wsin:LiabContractIdentifier>001-C-508109</wsin:LiabContractIdentifier>
         <wsin:ClientSearchMethod>CLIENT_ID</wsin:ClientSearchMethod>
         <wsin:ClientIdentifier>96100</wsin:ClientIdentifier>
         <wsin:ProductCode>RETAIL_TERMINAL</wsin:ProductCode>
         <wsin:ProductCode2></wsin:ProductCode2>
         <wsin:ProductCode3></wsin:ProductCode3>
         <wsin:InObject>
            <wsin:Branch>0101</wsin:Branch>
            <wsin:InstitutionCode>0001</wsin:InstitutionCode>
            <wsin:ContractName>Retail POS Terminal 01</wsin:ContractName>
            <wsin:CBSNumber>21324556600</wsin:CBSNumber>
            <wsin:AddInfo01>{add_info}</wsin:AddInfo01>
            <wsin:AddInfo02></wsin:AddInfo02>
         </wsin:InObject>  
      </wsin:CreateIssuingContractWithLiabilityV2>
   </soapenv:Body>
</soapenv:Envelope>
"""

variations = [
    "DEV.DEVICE_TYPE__ID=33;DEV.TERMINAL_ID=99999901;",
    "DEV.DEVICE_TYPE=33;DEV.TERMINAL_ID=99999901;",
    "DEV.DEVICE_TYPE=STD;DEV.TERMINAL_ID=99999901;",
    "DEVP.DEVICE_TYPE__ID=33;DEVP.TERMINAL_ID=99999901;",
    "DEVP.DEVICE_TYPE=33;DEVP.TERMINAL_ID=99999901;",
    "DEVP.DEVICE_TYPE=STD;DEVP.TERMINAL_ID=99999901;",
    "DEVICE.DEVICE_TYPE__ID=33;DEVICE.TERMINAL_ID=99999901;",
    "DEVICE.DEVICE_TYPE=33;DEVICE.TERMINAL_ID=99999901;",
    "DEVICE_REC.DEVICE_TYPE__ID=33;DEVICE_REC.TERMINAL_ID=99999901;",
    "ATM.DEVICE_TYPE__ID=33;ATM.TERMINAL_ID=99999901;",
    "POS.DEVICE_TYPE__ID=33;POS.TERMINAL_ID=99999901;",
    "DEV.DEVICE_TYPE_CODE=STD;DEV.TERMINAL_ID=99999901;"
]

def run_tests():
    for var in variations:
        print(f"Testing variation: {var}")
        xml_data = build_xml(var)
        
        with open("temp_test3.xml", "w", encoding="utf-8") as f:
            f.write(xml_data)
            
        cmd = [
            "curl.exe", "-s",
            "--location", URL,
            "--header", "Content-Type: application/xml",
            "--data", "@temp_test3.xml"
        ]
        
        try:
            res = subprocess.run(cmd, capture_output=True, text=True, timeout=10)
            output = res.stdout
            if "I Successfully Completed" in output or "CreatedContract" in output:
                print(f"--> SUCCESS with: {var}")
                print(output)
                return
            else:
                start = output.find("<RetMsg>")
                end = output.find("</RetMsg>")
                if start != -1 and end != -1:
                    print(f"  Failed: {output[start+8:end]}")
                else:
                    print("  Failed: Unknown error / SOAP fault")
        except Exception as e:
            print(f"  Error running command: {e}")
            
        time.sleep(1)

if __name__ == "__main__":
    run_tests()
