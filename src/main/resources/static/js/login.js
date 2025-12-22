document.getElementById('loginForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const messageEl = document.getElementById('message');

    messageEl.textContent = 'Logging in...';
    messageEl.className = 'message';

    try {
        const response = await fetch('/api/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();

        if (response.ok) {
            messageEl.textContent = 'Login successful! Redirecting...';
            messageEl.className = 'message success';
            // Placeholder redirect
            window.location.href = '/hello';
        } else {
            messageEl.textContent = data.message || 'Login failed';
            messageEl.className = 'message error';
        }
    } catch (error) {
        console.error('Error:', error);
        messageEl.textContent = 'An error occurred. Please try again.';
        messageEl.className = 'message error';
    }
});
