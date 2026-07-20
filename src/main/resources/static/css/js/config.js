// ==========================================
// Backend Configuration
// ==========================================

// Base URL of Spring Boot application
const BASE_URL = "http://localhost:8080";


// ==========================================
// Authentication
// ==========================================

// JWT received after successful login.
// It will be sent in every protected request.
let jwtToken = "";


// ==========================================
// Logged-in User
// ==========================================

let loggedUsername = "";

let loggedRole = "";


// ==========================================
// Account
// ==========================================

// Currently selected account.
let currentAccountId = "";


// ==========================================
// Transactions Pagination
// ==========================================

let currentPage = 0;

const PAGE_SIZE = 5;