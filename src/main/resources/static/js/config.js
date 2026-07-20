// Base URL (empty string because frontend is served on the same host/port)
const BASE_URL = "";

// Auth State Variables
let jwtToken = "";
let loggedUsername = "";
let loggedRole = "";

// Account & Pagination State Variables
let currentAccountId = "";
let currentPage = 0;
const PAGE_SIZE = 5;

// Common Headers
function getAuthHeaders() {
    return {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${jwtToken}`
    };
}

// Display Messages
function showMessage(message, color = "red") {
    const messageBox = document.getElementById("message");
    if (messageBox) {
        messageBox.innerText = message;
        messageBox.style.color = color;
    }
}