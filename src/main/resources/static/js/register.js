document.getElementById('registerForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const username = document.getElementById('username').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const messageEl = document.getElementById('message');

    messageEl.textContent = 'Registering...';
    messageEl.className = 'message';

    try {
        const response = await fetch('/api/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, email, password })
        });

        const data = await response.json();

        if (response.ok) {
            messageEl.textContent = 'Registration successful!';
            messageEl.className = 'message success';
            setTimeout(() => {
                window.location.href = '/login';
            }, 2000);
        } else {
            let errorMessage = data.message || 'Registration failed';
            if (data.validationErrors) {
                errorMessage = Object.values(data.validationErrors).join(', ');
            }
            messageEl.textContent = errorMessage;
            messageEl.className = 'message error';
        }
    } catch (error) {
        console.error('Error:', error);
        messageEl.textContent = 'An error occurred. Please try again.';
        messageEl.className = 'message error';
    }
});
