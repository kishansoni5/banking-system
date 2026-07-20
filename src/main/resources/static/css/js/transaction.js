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

	let rows = "";

	transactions.forEach(transaction => {
	    rows += `
	        <tr>
	            <td>${transaction.id}</td>
	            <td>${transaction.type}</td>
	            <td>₹${transaction.amount}</td>
	            <td>${transaction.timestamp}</td>
	        </tr>
	    `;
	});

	table.innerHTML = rows;

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