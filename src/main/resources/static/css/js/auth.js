// ==========================================
// Login
// ==========================================

async function login() {

    // Read username and password entered by the user.
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    try {

        // Call Spring Boot login API.
        // This endpoint is public, so no Authorization header is needed.
        const response = await fetch(`${BASE_URL}/auth/login`, {

            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                username: username,
                password: password
            })

        });

        // Convert JSON response into JavaScript object.
        const data = await response.json();

        // If login fails, Spring returns 401/403.
        if (!response.ok) {

            showMessage(data.message || "Login Failed");

            return;

        }

        // Save JWT for future API calls.
        jwtToken = data.token;

        // Save logged-in user information.
        loggedUsername = data.username;
        loggedRole = data.role;

        // Display user information.
        document.getElementById("loggedUsername").innerText = loggedUsername;
        document.getElementById("loggedRole").innerText = loggedRole;

        // Hide login form.
        document.getElementById("login-section").classList.add("hidden");

        // Show dashboard.
        document.getElementById("dashboard").classList.remove("hidden");

        showMessage("Login Successful", "green");

    }

    catch (error) {

        showMessage(error.message);

    }

}



// ==========================================
// Common Headers
// ==========================================

// Every protected request requires:
//
// Authorization: Bearer <jwt>
//
function getAuthHeaders() {

    return {

        "Content-Type": "application/json",

        "Authorization": `Bearer ${jwtToken}`

    };

}



// ==========================================
// Message
// ==========================================

function showMessage(message, color = "red") {

    const messageBox = document.getElementById("message");

    messageBox.innerText = message;

    messageBox.style.color = color;

}