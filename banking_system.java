// ==================== INTERFACE ====================
interface Transactionable {
    void withdraw(double amount);
    void transfer(Account toAccount, double amount);
}

interface Auditable {
    void generateStatement();
}

// ==================== ABSTRACT CLASS ====================
abstract class Account {
    // Encapsulation → private variables
    private String accountNumber;
    private double balance;
    private String holderName;

    // Constructor
    public Account(String accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;   // 'this' keyword
        this.holderName = holderName;
        this.balance = balance;
    }

    // Concrete methods (common for all accounts)
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println(amount + " deposited successfully.");
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    // Protected setter so child classes can update balance
    protected void setBalance(double balance) {
        this.balance = balance;
    }

    // Abstract method → must be implemented by child classes
    public abstract double calculateInterest();

    // Method Overloading example
    public void printDetails() {
        System.out.println("Account: " + accountNumber + " | Holder: " + holderName);
    }

    public void printDetails(boolean showBalance) {
        printDetails();
        if (showBalance) {
            System.out.println("Balance: " + balance);
        }
    }
}

// ==================== SAVINGS ACCOUNT ====================
class SavingsAccount extends Account implements Transactionable, Auditable {

    private double interestRate = 0.04;

    // Constructor using super()
    public SavingsAccount(String accountNumber, String holderName, double balance) {
        super(accountNumber, holderName, balance);   // 'super' keyword
    }

    // Method Overriding (Runtime Polymorphism)
    @Override
    public double calculateInterest() {
        return getBalance() * interestRate;
    }

    // Implementing interface methods
    @Override
    public void withdraw(double amount) {
        if (amount > 0 && getBalance() >= amount) {
            setBalance(getBalance() - amount);
            System.out.println(amount + " withdrawn from Savings Account.");
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    @Override
    public void transfer(Account toAccount, double amount) {
        if (getBalance() >= amount) {
            this.withdraw(amount);
            toAccount.deposit(amount);
            System.out.println("Transferred " + amount + " to " + toAccount.getAccountNumber());
        } else {
            System.out.println("Transfer failed due to insufficient balance.");
        }
    }

    @Override
    public void generateStatement() {
        System.out.println("----- Savings Account Statement -----");
        printDetails(true);
        System.out.println("Interest earned: " + calculateInterest());
        System.out.println("-------------------------------------");
    }
}

// ==================== CURRENT ACCOUNT ====================
class CurrentAccount extends Account implements Transactionable {

    public CurrentAccount(String accountNumber, String holderName, double balance) {
        super(accountNumber, holderName, balance);
    }

    @Override
    public double calculateInterest() {
        return 0; // Current account has no interest
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && getBalance() >= amount) {
            setBalance(getBalance() - amount);
            System.out.println(amount + " withdrawn from Current Account.");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    @Override
    public void transfer(Account toAccount, double amount) {
        if (getBalance() >= amount) {
            this.withdraw(amount);
            toAccount.deposit(amount);
            System.out.println("Transferred " + amount + " to " + toAccount.getAccountNumber());
        }
    }
}

// ==================== MAIN CLASS ====================
public class BankingSystemDemo {
    public static void main(String[] args) {

        // Creating objects
        SavingsAccount rahulAccount = new SavingsAccount("SAV1001", "Rahul Sharma", 50000);
        CurrentAccount priyaAccount = new CurrentAccount("CUR2002", "Priya Patel", 30000);

        System.out.println("========== INITIAL DETAILS ==========");
        rahulAccount.printDetails(true);
        priyaAccount.printDetails(true);

        System.out.println("\n========== PERFORMING TRANSACTIONS ==========");
        rahulAccount.deposit(10000);
        rahulAccount.withdraw(5000);
        rahulAccount.transfer(priyaAccount, 8000);

        System.out.println("\n========== INTEREST CALCULATION (Polymorphism) ==========");
        Account acc1 = rahulAccount;     // Parent reference → Child object
        Account acc2 = priyaAccount;

        System.out.println("Rahul Interest: " + acc1.calculateInterest());
        System.out.println("Priya Interest: " + acc2.calculateInterest());

        System.out.println("\n========== STATEMENT (Interface) ==========");
        rahulAccount.generateStatement();
    }
}
