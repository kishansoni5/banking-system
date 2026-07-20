async function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    try {
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

        const data = await response.json();

        if (!response.ok) {
            showMessage(data.message || "Login Failed");
            return;
        }

        jwtToken = data.token;
        loggedUsername = data.username;
        loggedRole = data.role;

        document.getElementById("loggedUsername").innerText = loggedUsername;
        document.getElementById("loggedRole").innerText = loggedRole;

        document.getElementById("login-section").classList.add("hidden");
        document.getElementById("dashboard").classList.remove("hidden");

        showMessage("Login Successful", "green");

    } catch (error) {
        showMessage(error.message);
    }
}