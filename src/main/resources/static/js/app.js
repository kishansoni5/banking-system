document.addEventListener("DOMContentLoaded", () => {
    // Auth Event
    document.getElementById("loginBtn").addEventListener("click", login);

    // Account Events
    document.getElementById("loadAccountBtn").addEventListener("click", loadAccount);
    document.getElementById("balanceBtn").addEventListener("click", getBalance);
    document.getElementById("depositBtn").addEventListener("click", deposit);
    document.getElementById("withdrawBtn").addEventListener("click", withdraw);
    document.getElementById("transferBtn").addEventListener("click", transfer);

    // Transaction Events
    document.getElementById("previousBtn").addEventListener("click", previousPage);
    document.getElementById("nextBtn").addEventListener("click", nextPage);
});