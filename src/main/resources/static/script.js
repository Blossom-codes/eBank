// Define the API endpoint
const apiUrl = 'http://localhost:8080/eBank/api/data';

// Function to fetch and display the list of customers
async function fetchCustomers() {
    try {
        // Send a GET request to the API endpoint
        const response = await fetch(apiUrl, {
            method: 'GET', // HTTP method
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer YOUR_API_TOKEN' // Include if the API requires authentication
            }
        });

        // Check if the response is OK (status code 200)
        if (response.ok) {
            // Parse the JSON response
            const customers = await response.json();

            // Get the DOM element where the data will be inserted
            const customerList = document.getElementById('customer-list');

            // Loop through the customer data and create list items
            customers.forEach(customer => {
                const listItem = document.createElement('li');
//                const balance = document.createElement('button');
                listItem.textContent = `${customer.firstName} ${customer.lastName} - ${customer.email} -${customer.balance}`; // Example: display name and email
//                balance.textContent = `${Customer.balance}`;
                customerList.appendChild(listItem);
            });
        } else {
            console.error('Failed to fetch customers:', response.status, response.statusText);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

// Call the function to fetch and display customers when the page loads
window.onload = fetchCustomers;
