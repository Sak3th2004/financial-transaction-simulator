# Transaction Generator

Python-based load testing tool for the financial transaction simulator.

## Features

- **Sequential Mode**: Generate transactions one-by-one
- **Parallel Mode**: Multi-threaded high-throughput testing  
- **Batch Mode**: Bulk submission for maximum performance

## Installation

```bash
pip install -r requirements.txt
```

## Usage

### Sequential Test (1000 transactions)
```bash
python transaction_generator.py --mode sequential --count 1000
```

### Parallel Test (10,000 transactions, 20 workers)
```bash
python transaction_generator.py --mode parallel --count 10000 --workers 20
```

### Batch Test (100,000 transactions, batches of 500)
```bash
python transaction_generator.py --mode batch --count 100000 --batch-size 500
```

## Generated Data

- **Users**: 1000 mock users
- **Merchants**: 100 mock merchants
- **Transaction Types**: DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT, REFUND
- **Amount Range**: $0.01 - $5,000 (with 5% high-value $10k-$150k for fraud testing)
- **Currencies**: USD, EUR, INR, GBP, JPY
- **Categories**: groceries, entertainment, utilities, healthcare, etc.

## Performance Targets

- Sequential: ~500-1000 txn/sec
- Parallel (10 workers): ~2000-5000 txn/sec
- Batch (size 100): ~5000-10000 txn/sec
