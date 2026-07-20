// ==========================================
// Load Transactions
// ==========================================

async function loadTransactions() {

    if (!currentAccountId) {
        showMessage("Load an account first");
        return;
    }

    try {

        // Call the paginated transaction endpoint
        const response = await fetch(
            `${BASE_URL}/accounts/${currentAccountId}/transactions?page=${currentPage}&size=${PAGE_SIZE}`,
            {
                headers: getAuthHeaders()
            }
        );

        const page = await response.json();

        if (!response.ok) {
            showMessage(page.message || "Unable to load transactions");
            return;
        }

        renderTransactions(page.content);

        // Disable buttons when necessary
        document.getElementById("previousBtn").disabled = page.first;
        document.getElementById("nextBtn").disabled = page.last;

    }
    catch (error) {
        showMessage(error.message);
    }

}


// ==========================================
// Render Transaction Table
// ==========================================

function renderTransactions(transactions) {

    const table = document.getElementById("transactionTable");

    table.innerHTML = "";

    transactions.forEach(transaction => {

        table.innerHTML += `
            <tr>
                <td>${transaction.id}</td>
                <td>${transaction.type}</td>
                <td>₹${transaction.amount}</td>
                <td>${transaction.timestamp}</td>
            </tr>
        `;

    });

}


// ==========================================
// Next Page
// ==========================================

function nextPage() {

    currentPage++;

    loadTransactions();

}


// ==========================================
// Previous Page
// ==========================================

function previousPage() {

    if (currentPage > 0) {

        currentPage--;

    }

    loadTransactions();

}