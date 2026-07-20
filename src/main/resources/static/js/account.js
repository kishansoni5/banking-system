async function loadAccount() {
    currentAccountId = document.getElementById("accountId").value;

    if (!currentAccountId) {
        showMessage("Please enter Account ID");
        return;
    }

    try {
        const response = await fetch(`${BASE_URL}/accounts/${currentAccountId}`, {
            headers: getAuthHeaders()
        });

        const account = await response.json();

        if (!response.ok) {
            showMessage(account.message || "Unable to load account");
            return;
        }

        showMessage("Account Loaded Successfully", "green");
        getBalance();
        currentPage = 0;
        loadTransactions();

    } catch (error) {
        showMessage(error.message);
    }
}

async function getBalance() {
    if (!currentAccountId) {
        showMessage("Load an account first");
        return;
    }

    try {
        const response = await fetch(`${BASE_URL}/accounts/${currentAccountId}/balance`, {
            headers: getAuthHeaders()
        });

        const balance = await response.json();

        if (!response.ok) {
            showMessage("Unable to fetch balance");
            return;
        }

        document.getElementById("balance").innerText = balance;

    } catch (error) {
        showMessage(error.message);
    }
}

async function deposit() {
    const amount = Number(document.getElementById("depositAmount").value);

    try {
        const response = await fetch(`${BASE_URL}/accounts/${currentAccountId}/deposit`, {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify({ amount: amount })
        });

        const result = await response.text();

        if (!response.ok) {
            showMessage(result);
            return;
        }

        showMessage(result, "green");
        getBalance();
        loadTransactions();

    } catch (error) {
        showMessage(error.message);
    }
}

async function withdraw() {
    const amount = Number(document.getElementById("withdrawAmount").value);

    try {
        const response = await fetch(`${BASE_URL}/accounts/${currentAccountId}/withdraw`, {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify({ amount: amount })
        });

        const result = await response.text();

        if (!response.ok) {
            showMessage(result);
            return;
        }

        showMessage(result, "green");
        getBalance();
        loadTransactions();

    } catch (error) {
        showMessage(error.message);
    }
}

async function transfer() {
    const targetAccountId = document.getElementById("targetAccountId").value;
    const amount = Number(document.getElementById("transferAmount").value);

    try {
        const response = await fetch(`${BASE_URL}/accounts/transfer`, {
            method: "POST",
            headers: getAuthHeaders(),
            body: JSON.stringify({
                sourceAccountId: currentAccountId,
                targetAccountId: targetAccountId,
                amount: amount
            })
        });

        const result = await response.text();

        if (!response.ok) {
            showMessage(result);
            return;
        }

        showMessage(result, "green");
        getBalance();
        loadTransactions();

    } catch (error) {
        showMessage(error.message);
    }
}