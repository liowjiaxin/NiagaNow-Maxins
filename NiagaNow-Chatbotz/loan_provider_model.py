# loan_provider_model.py

import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder

def load_model():
    df = pd.read_csv('loan_providers.csv', encoding='cp1252')

    def parse_amount_range(s):
        s = s.replace('RM', '').replace(',', '').replace(' ', '')
        parts = s.split('-')
        try:
            if len(parts) == 2:
                return int(parts[0]), int(parts[1])
            elif len(parts) == 1 and parts[0].isdigit():
                return int(parts[0]), np.nan
        except:
            return np.nan, np.nan
        return np.nan, np.nan

    df[['min_amount', 'max_amount']] = df['Loan / Financing Size'].apply(lambda x: pd.Series(parse_amount_range(str(x))))

    def parse_year_range(s):
        s = s.replace('months', 'month').replace('years', 'year').replace('–', '-').replace('to', '-')
        parts = [x.strip() for x in s.split('-')]

        def convert(x):
            if 'month' in x:
                return float(x.split()[0]) / 12
            elif 'year' in x:
                return float(x.split()[0])
            elif x.isdigit():
                return float(x)
            else:
                return np.nan

        try:
            if len(parts) == 2:
                return convert(parts[0]), convert(parts[1])
        except:
            return np.nan, np.nan
        return np.nan, np.nan

    df[['min_years', 'max_years']] = df['Tenure of Financing'].apply(lambda x: pd.Series(parse_year_range(str(x))))
    df['Provider'] = df['Provider'].str.split('\n').str[0].str.upper()
    df_clean = df.dropna(subset=['min_amount', 'max_amount', 'min_years', 'max_years'])

    synthetic_data = []
    for _, row in df_clean.iterrows():
        for _ in range(50):
            loan = np.random.randint(row['min_amount'], row['max_amount'] + 1)
            years = np.random.uniform(row['min_years'], row['max_years'])
            synthetic_data.append([loan, years, row['Provider']])

    synthetic_df = pd.DataFrame(synthetic_data, columns=['loan_amount', 'tenure_years', 'provider'])

    le = LabelEncoder()
    synthetic_df['provider_encoded'] = le.fit_transform(synthetic_df['provider'])

    X = synthetic_df[['loan_amount', 'tenure_years']]
    y = synthetic_df['provider_encoded']

    model = RandomForestClassifier(n_estimators=100, random_state=42)
    model.fit(X, y)

    return model, le
