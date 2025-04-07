import os
import pandas as pd
from sqlalchemy import create_engine
import logging

# Erstellen von logging-Ausgaben
# logging.basicConfig()
# logging.getLogger('sqlalchemy.engine').setLevel(logging.INFO)

def load_db_credentials(file_path):
    credentials = {}
    try:
        with open(file_path, 'r') as file:
            for line in file:
                # Nur Zeilen mit Inhalt
                if line.strip():
                    key, value = line.strip().split('=')
                    credentials[key] = value
    except Exception as e:
        print(f"fehler beim laden der anmeldedaten: {e}")
    return credentials

def csv_to_sql(csv_folder, db_credentials, if_exists_option):
    try:
        # Verbindung zur Datenbank
        engine = create_engine(
            f'postgresql://{db_credentials["db_user"]}:{db_credentials["db_password"]}@{db_credentials["db_host"]}/{db_credentials["db_name"]}')

        # Alle CSV-Dateien im Ordner durchlaufen
        for csv_file in os.listdir(csv_folder):
            if csv_file.endswith(".csv"):
                # Tabellennamen bestimmen (entfernt Endung) + Pfad merken
                table_name = os.path.splitext(csv_file)[0]
                csv_file_path = os.path.join(csv_folder, csv_file)

                # CSV-Datei einlesen
                csv = pd.read_csv(csv_file_path, sep="|")
                with engine.connect() as connection:
                    csv.to_sql(table_name, engine, schema='socialnetwork', if_exists=if_exists_option, index=False)
                print(f"die daten aus {csv_file} wurden erfolgreich in die tabelle {table_name} importiert.")

    except Exception as e:
        print(f"fehler: {e}")

def execute_sql_script(db_credentials, script_path):
    try:
        # Verbindung zur Datenbank
        engine = create_engine(
            f'postgresql://{db_credentials["db_user"]}:{db_credentials["db_password"]}@{db_credentials["db_host"]}/{db_credentials["db_name"]}')

        # Skripte ausführen
        with open(script_path, 'r') as file:
            script = file.read()
            with engine.connect() as connection:
                print (script)
                connection.execute(script)
            print(f"das skript {script_path} wurde erfolgreich ausgeführt.")
    except Exception as e:
        print(f"fehler beim ausführen des skript: {e}")



# Main Import
# Setzen der Parameter
db_credentials_file_main = "db_credentials.txt"
csv_folder_main = "import"
if_exists_option_main = "append"  # Optionen: 'fail', 'replace', 'append'
# setzten der Anmededaten
db_credentials_main = load_db_credentials(db_credentials_file_main)


# DDLskript ausführen
#script_path_1 = "createskript.sql"
#if db_credentials_main:
#    execute_sql_script(db_credentials_main, script_path_1)
#else:
#    print("fehler: anmeldedaten konnten nicht geladen werden.")

# Angepasste Daten
# Setzen der Parameter
db_credentials_file = "db_credentials.txt"
csv_folder = "rest"
if_exists_option = "replace"  # Optionen: 'fail', 'replace', 'append'
# setzten der Anmeldedaten
db_credentials = load_db_credentials(db_credentials_file)

# Import
# Main
if db_credentials_main:
    print("main")
    csv_to_sql(csv_folder_main, db_credentials_main, if_exists_option_main)
else:
    print("fehler: anmeldedaten konnten nicht geladen werden.")

# Rest
if db_credentials:
    print("rest")
    csv_to_sql(csv_folder, db_credentials, if_exists_option)
else:
    print("fehler: anmeldedaten konnten nicht geladen werden.")


# db-Skript ausführen (angepasste Daten einfügen)
#script_path_2 = "db_bereinigung.sql"
#if db_credentials_main:
#    execute_sql_script(db_credentials_main, script_path_2)
#else:
#    print("fehler: anmeldedaten konnten nicht geladen werden.")
