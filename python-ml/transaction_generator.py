import requests
import random
import json
import time
import uuid
from datetime import datetime
from concurrent.futures import ThreadPoolExecutor
import argparse

# Configuration
API_URL = "http://localhost:8081/api/v1/transactions"
BATCH_API_URL = "http://localhost:8081/api/v1/transactions/batch"

# Mock data
TRANSACTION_TYPES = ["DEPOSIT", "WITHDRAWAL", "TRANSFER", "PAYMENT", "REFUND"]
CURRENCIES = ["USD", "EUR", "INR", "GBP", "JPY"]
CATEGORIES = ["groceries", "entertainment", "utilities", "healthcare", "education", "shopping", "dining"]
MERCHANTS = [f"merchant_{i}" for i in range(1, 101)]
USERS = [f"user_{i}" for i in range(1, 1001)]

# IP ranges for simulation
IP_PREFIXES = ["192.168.1", "10.0.0", "172.16.0", "203.0.113"]

def generate_transaction():
    """Generate a realistic transaction object"""
    tx_type = random.choice(TRANSACTION_TYPES)
    amount = round(random.uniform(0.01, 5000.00), 2)
    
    # Occasionally generate high-value transactions (for fraud detection testing)
    if random.random() < 0.05:  # 5% chance
        amount = round(random.uniform(10000, 150000), 2)
    
    transaction = {
        "userId": random.choice(USERS),
        "amount": amount,
        "transactionType": tx_type,
        "currency": random.choice(CURRENCIES),
        "category": random.choice(CATEGORIES),
        "location": f"{random.uniform(-90, 90)},{random.uniform(-180, 180)}",
        "ipAddress": f"{random.choice(IP_PREFIXES)}.{random.randint(1, 254)}",
        "deviceId": f"device_{random.randint(1, 500)}"
    }
    
    # Add target account for transfers
    if tx_type == "TRANSFER":
        transaction["targetAccountId"] = f"account_{random.randint(1, 1000)}"
    
    # Add merchant for payments
    if tx_type in ["PAYMENT", "REFUND"]:
        transaction["merchantId"] = random.choice(MERCHANTS)
    
    return transaction

def send_single_transaction():
    """Send a single transaction to the API"""
    transaction = generate_transaction()
    try:
        response = requests.post(API_URL, json=transaction, timeout=5)
        if response.status_code in [200, 202]:
            return {"success": True, "response": response.json()}
        else:
            return {"success": False, "error": f"Status {response.status_code}"}
    except Exception as e:
        return {"success": False, "error": str(e)}

def send_batch_transactions(batch_size=100):
    """Send a batch of transactions"""
    transactions = [generate_transaction() for _ in range(batch_size)]
    try:
        response = requests.post(BATCH_API_URL, json=transactions, timeout=30)
        if response.status_code in [200, 202]:
            return {"success": True, "count": batch_size, "response": response.json()}
        else:
            return {"success": False, "error": f"Status {response.status_code}"}
    except Exception as e:
        return {"success": False, "error": str(e)}

def load_test_sequential(count=1000):
    """Generate transactions sequentially"""
    print(f"\n🚀 Starting sequential load test: {count} transactions")
    print("=" * 60)
    
    success_count = 0
    failed_count = 0
    start_time = time.time()
    
    for i in range(count):
        result = send_single_transaction()
        if result["success"]:
            success_count += 1
        else:
            failed_count += 1
            print(f"❌ Failed: {result.get('error')}")
        
        # Progress indicator
        if (i + 1) % 100 == 0:
            elapsed = time.time() - start_time
            rate = (i + 1) / elapsed
            print(f"✅ Progress: {i+1}/{count} | Rate: {rate:.1f} txn/sec | Success: {success_count} | Failed: {failed_count}")
    
    end_time = time.time()
    duration = end_time - start_time
    
    print("\n" + "=" * 60)
    print(f"📊 Sequential Test Results:")
    print(f"   Total Transactions: {count}")
    print(f"   Successful: {success_count} ({success_count/count*100:.1f}%)")
    print(f"   Failed: {failed_count}")
    print(f"   Duration: {duration:.2f} seconds")
    print(f"   Average Rate: {count/duration:.1f} transactions/second")
    print("=" * 60)

def load_test_parallel(count=1000, workers=10):
    """Generate transactions in parallel"""
    print(f"\n🚀 Starting parallel load test: {count} transactions with {workers} workers")
    print("=" * 60)
    
    success_count = 0
    failed_count = 0
    start_time = time.time()
    
    with ThreadPoolExecutor(max_workers=workers) as executor:
        futures = [executor.submit(send_single_transaction) for _ in range(count)]
        
        for i, future in enumerate(futures):
            result = future.result()
            if result["success"]:
                success_count += 1
            else:
                failed_count += 1
            
            # Progress indicator
            if (i + 1) % 100 == 0:
                elapsed = time.time() - start_time
                rate = (i + 1) / elapsed
                print(f"✅ Progress: {i+1}/{count} | Rate: {rate:.1f} txn/sec | Success: {success_count} | Failed: {failed_count}")
    
    end_time = time.time()
    duration = end_time - start_time
    
    print("\n" + "=" * 60)
    print(f"📊 Parallel Test Results:")
    print(f"   Total Transactions: {count}")
    print(f"   Successful: {success_count} ({success_count/count*100:.1f}%)")
    print(f"   Failed: {failed_count}")
    print(f"   Duration: {duration:.2f} seconds")
    print(f"   Average Rate: {count/duration:.1f} transactions/second")
    print("=" * 60)

def load_test_batch(total=10000, batch_size=100):
    """Generate transactions using batch API"""
    num_batches = total // batch_size
    print(f"\n🚀 Starting batch load test: {total} transactions in {num_batches} batches")
    print("=" * 60)
    
    success_count = 0
    failed_count = 0
    start_time = time.time()
    
    for i in range(num_batches):
        result = send_batch_transactions(batch_size)
        if result["success"]:
            success_count += batch_size
        else:
            failed_count += batch_size
            print(f"❌ Batch {i+1} failed: {result.get('error')}")
        
        # Progress indicator
        if (i + 1) % 10 == 0:
            elapsed = time.time() - start_time
            processed = (i + 1) * batch_size
            rate = processed / elapsed
            print(f"✅ Progress: {processed}/{total} | Rate: {rate:.1f} txn/sec | Success: {success_count}")
    
    end_time = time.time()
    duration = end_time - start_time
    
    print("\n" + "=" * 60)
    print(f"📊 Batch Test Results:")
    print(f"   Total Transactions: {total}")
    print(f"   Batch Size: {batch_size}")
    print(f"   Successful: {success_count} ({success_count/total*100:.1f}%)")
    print(f"   Failed: {failed_count}")
    print(f"   Duration: {duration:.2f} seconds")
    print(f"   Average Rate: {total/duration:.1f} transactions/second")
    print("=" * 60)

def main():
    parser = argparse.ArgumentParser(description='Financial Transaction Load Generator')
    parser.add_argument('--mode', choices=['sequential', 'parallel', 'batch'], default='sequential',
                       help='Test mode: sequential, parallel, or batch')
    parser.add_argument('--count', type=int, default=1000, 
                       help='Number of transactions to generate')
    parser.add_argument('--workers', type=int, default=10,
                       help='Number of parallel workers (parallel mode only)')
    parser.add_argument('--batch-size', type=int, default=100,
                       help='Batch size (batch mode only)')
    
    args = parser.parse_args()
    
    print("\n" + "=" * 60)
    print("💰 FINANCIAL TRANSACTION LOAD GENERATOR")
    print("=" * 60)
    print(f"Target API: {API_URL}")
    print(f"Mode: {args.mode.upper()}")
    print(f"Total Transactions: {args.count}")
    
    if args.mode == 'sequential':
        load_test_sequential(args.count)
    elif args.mode == 'parallel':
        load_test_parallel(args.count, args.workers)
    elif args.mode == 'batch':
        load_test_batch(args.count, args.batch_size)

if __name__ == "__main__":
    main()
