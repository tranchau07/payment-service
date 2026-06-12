import sys
import os

# Hướng dẫn:
# Cài đặt thư viện: pip install oracledb
# Script này sử dụng thông tin từ application.yaml để kết nối DB

try:
    import oracledb
except ImportError:
    print("Error: Thư viện 'oracledb' chưa được cài đặt. Vui lòng chạy: pip install oracledb")
    sys.exit(1)

# DB Credentials (từ application.yaml)
DB_CONFIG = {
    "user": "INT",
    "password": "way4",
    "dsn": "10.145.48.96:1521/way4db"
}

def run_query(sql_query):
    try:
        # Kết nối ở chế độ Thin mode (không cần cài Oracle Client)
        connection = oracledb.connect(
            user=DB_CONFIG["user"],
            password=DB_CONFIG["password"],
            dsn=DB_CONFIG["dsn"]
        )
        
        cursor = connection.cursor()
        cursor.execute(sql_query)
        
        # Lấy column names
        columns = [col[0] for col in cursor.description]
        print(" | ".join(columns))
        print("-" * 50)
        
        # Lấy dữ liệu
        for row in cursor:
            print(" | ".join(map(str, row)))
            
        cursor.close()
        connection.close()
        
    except oracledb.Error as e:
        print(f"Database error: {e}")
    except Exception as e:
        print(f"General error: {e}")

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python query_db.py \"SELECT * FROM CLIENT WHERE CLIENT_NUMBER = '...'\"")
    else:
        query = sys.argv[1]
        run_query(query)
