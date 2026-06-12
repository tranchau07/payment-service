import oracledb

DB_CONFIG = {
    "user": "INT",
    "password": "way4",
    "dsn": "10.145.48.96:1521/way4db"
}

def dump():
    connection = oracledb.connect(**DB_CONFIG)
    cursor = connection.cursor()
    cursor.execute("SELECT * FROM APPL_PRODUCT WHERE CODE IN ('RETAIL_TERMINAL', 'CASH_TERMINAL', 'ATM_TERMINAL') AND AMND_STATE = 'A'")
    columns = [col[0] for col in cursor.description]
    for row in cursor:
        print("="*60)
        p = dict(zip(columns, row))
        print(p['CODE'])
        for col in sorted(columns):
            if p[col] is not None and col != 'CODE':
                print(f"  {col}: {p[col]}")
    cursor.close()
    connection.close()

if __name__ == "__main__":
    dump()
