// ==========================================
// Load Account
// ==========================================

async function loadAccount() {

    currentAccountId = document.getElementById("accountId").value;

    if (!currentAccountId) {
        showMessage("Please enter Account ID");
        return;
    }

    try {

        // Get account details from backend
        const response = await fetch(
            `${BASE_URL}/accounts/${currentAccountId}`,
            {
                headers: getAuthHeaders()
            }
        );

        const account = await response.json();

        if (!response.ok) {
            showMessage(account.message || "Unable to load account");
            return;
        }

        showMessage("Account Loaded Successfully", "green");

        // Also refresh balance
        getBalance();

        // Also load transactions
        currentPage = 0;
        loadTransactions();

    } catch (error) {

        showMessage(error.message);

    }

}


// ==========================================
// Get Balance
// ==========================================

async function getBalance() {

    if (!currentAccountId) {

        showMessage("Load an account first");

        return;

    }

    try {

        // Call:
        // GET /accounts/{id}/balance
        const response = await fetch(
            `${BASE_URL}/accounts/${currentAccountId}/balance`,
            {
                headers: getAuthHeaders()
            }
        );

        const balance = await response.json();

        if (!response.ok) {

            showMessage("Unable to fetch balance");

            return;

        }

        document.getElementById("balance").innerText = balance;

    }

    catch (error) {

        showMessage(error.message);

    }

}


// ==========================================
// Deposit
// ==========================================

async function deposit() {

    const amount = Number(
        document.getElementById("depositAmount").value
    );

    try {

        // Deposit endpoint
        const response = await fetch(
            `${BASE_URL}/accounts/${currentAccountId}/deposit`,
            {

                method: "POST",

                headers: getAuthHeaders(),

                body: JSON.stringify({

                    amount: amount

                })

            }
        );

        const result = await response.text();

        if (!response.ok) {

            showMessage(result);

            return;

        }

        showMessage(result, "green");

        getBalance();

        loadTransactions();

    }

    catch (error) {

        showMessage(error.message);

    }

}


// ==========================================
// Withdraw
// ==========================================

async function withdraw() {

    const amount = Number(
        document.getElementById("withdrawAmount").value
    );

    try {

        const response = await fetch(
            `${BASE_URL}/accounts/${currentAccountId}/withdraw`,
            {

                method: "POST",

                headers: getAuthHeaders(),

                body: JSON.stringify({

                    amount: amount

                })

            }
        );

        const result = await response.text();

        if (!response.ok) {

            showMessage(result);

            return;

        }

        showMessage(result, "green");

        getBalance();

        loadTransactions();

    }

    catch (error) {

        showMessage(error.message);

    }

}


// ==========================================
// Transfer
// ==========================================

async function transfer() {

    const targetAccountId =
        document.getElementById("targetAccountId").value;

    const amount =
        Number(document.getElementById("transferAmount").value);

    try {

        const response = await fetch(
            `${BASE_URL}/accounts/transfer`,
            {

                method: "POST",

                headers: getAuthHeaders(),

                body: JSON.stringify({

                    sourceAccountId: currentAccountId,

                    targetAccountId: targetAccountId,

                    amount: amount

                })

            }
        );

        const result = await response.text();

        if (!response.ok) {

            showMessage(result);

            return;

        }

        showMessage(result, "green");

        getBalance();

        loadTransactions();

    }

    catch (error) {

        showMessage(error.message);

    }

}