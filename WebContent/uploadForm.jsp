<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>File Upload</title>
    <link rel="stylesheet" type="text/css" href="style.css">

</head>
<body>
    <div class="form-container">
        <h2>Upload File</h2>
        
        <form onload="uploadForm.jsp" action="uploadServlet" method="post" enctype="multipart/form-data">
            <div class="buttons">
                <label for="action">Choose action:</label>
                <select id="action" name="action" onchange="updateFormFields()">
                    <option value="print">Print</option>
                    <option value="transfer">Transfer</option>
                    <option value="retrieve">Retrieve</option>
                    <option value="message">Message</option>
                </select>
            </div>
            
            <label for="userName">User Name:</label>
            <input type="text" id="userName" name="userName" required>
            
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
            
             <label for="message" class="message-field hidden">Message:</label>
            <input type="text" id="message" name="message" class="message-field hidden">
            
            <label for="printerName" class="print-transfer-field hidden">Printer Name:</label>
            <input type="text" id="printerName" name="printerName" class="print-transfer-field hidden">
            
            <label for="fileRetrieve" class="retrieve-field hidden">File to Retrieve:</label>
            <input type="text" id="fileRetrieve" name="fileRetrieve" class="retrieve-field hidden">
            
            <label for="outputPath" class="retrieve-field hidden">Path to Move File/Folder to:</label>
            <input type="text" id="outputPath" name="outputPath" class="retrieve-field hidden">
            
            <label for="flag" class="print-transfer-field hidden">Option:</label>
            <select id="flag" name="flag" class="print-transfer-field hidden">
                <option value="move">Move</option>
                <option value="delete">Delete</option>
                <option value="keep" selected>Keep</option>
            </select>
            
            <label for="file" class="print-transfer-field hidden">Select file:</label>
            <input type="file" id="file" name="file" class="print-transfer-field hidden">
            
            <label for="folderPath" class="print-transfer-field hidden">Folder Path:</label>
            <input type="text" id="folderPath" name="folderPath" class="print-transfer-field hidden">
            
           <% 
                String error = request.getParameter("error");
                String empty = request.getParameter("empty");
                String error2= request.getParameter("error2");
                String error3= request.getParameter("error3");
                if ("true".equals(error)) { %>
                    <p id="error-message" style="color: red;">Folder Path is required when command is 'Print'.</p>
                <% } 
                if ("true".equals(empty)) { %>
                    <p id="empty-message" style="color: red;">Message is required when command is 'Message'</p>
                <% }
                if ("true".equals(error2)) { %>
                <p id="error2-message" style="color: red;">Folder Path is required when command is 'Transfer'</p>
            <% }              
                if ("true".equals(error3)) { %>
                <p id="error3-message" style="color: red;">Path to Retrieve is required when command is 'Retrieve'</p>
            <% }
            %>
            
            <input type="submit" value="Submit">
        </form>
    </div>
<script>
    function handleActionChange() {
        const action = document.getElementById('action').value;

        // Clear all messages when action changes
        const errorMessage = document.getElementById('error-message');
        const emptyMessage = document.getElementById('empty-message');
        const errorMessage2 = document.getElementById('error2-message');
        const errorMessage3 = document.getElementById('error3-message');

        if (errorMessage) errorMessage.style.display = 'none';
        if (emptyMessage) emptyMessage.style.display = 'none';
        if (errorMessage2) errorMessage2.style.display = 'none';
        if (errorMessage3) errorMessage3.style.display = 'none';

        // Update the URL with the selected action and remove error parameter
        const currentURL = new URL(window.location.href);
        currentURL.searchParams.set('action', action);
        currentURL.searchParams.delete('error');  // Remove the error parameter
        currentURL.searchParams.delete('empty');  // Remove the empty parameter
        currentURL.searchParams.delete('error2'); // Remove the error2 parameter
        currentURL.searchParams.delete('error3'); // Remove the error3 parameter

        window.history.pushState({}, '', currentURL);
        
        // Update form fields based on new action
        updateFormFields();
    }

    function updateFormFields() {
        const action = document.getElementById('action').value;

        // Show/Hide fields based on the selected action
        const printTransferFields = document.querySelectorAll('.print-transfer-field');
        const retrieveFields = document.querySelectorAll('.retrieve-field');
        const messageFields = document.querySelectorAll('.message-field');
        const outputPathLabel = document.querySelector('label[for="outputPath"]');

        // Initially hide all fields
        printTransferFields.forEach(field => field.classList.add('hidden'));
        retrieveFields.forEach(field => field.classList.add('hidden'));
        messageFields.forEach(field => field.classList.add('hidden'));

        // Show fields based on selected action
        if (action === 'retrieve') {
            retrieveFields.forEach(field => field.classList.remove('hidden'));
            outputPathLabel.textContent = 'Path to Retrieve:';
        } else if (action === 'print') {
            printTransferFields.forEach(field => field.classList.remove('hidden'));
            document.getElementById('outputPath').classList.remove('hidden');
            document.querySelector('label[for="outputPath"]').classList.remove('hidden');
            outputPathLabel.textContent = 'Path to Move File/Folder to:';
        } else if (action === 'transfer') {
            printTransferFields.forEach(field => {
                if (!field.classList.contains('hidden')) {
                    field.classList.remove('hidden');
                }
            });
            document.getElementById('outputPath').classList.remove('hidden');
            document.querySelector('label[for="outputPath"]').classList.remove('hidden');
            outputPathLabel.textContent = 'Path to Move File/Folder to:';
            document.getElementById('printerName').classList.add('hidden');
            document.getElementById('flag').classList.add('hidden');
            document.querySelector('label[for="printerName"]').classList.add('hidden');
            document.querySelector('label[for="flag"]').classList.add('hidden');
            document.getElementById('file').classList.remove('hidden');
            document.getElementById('folderPath').classList.remove('hidden');
            document.querySelector('label[for="file"]').classList.remove('hidden');
            document.querySelector('label[for="folderPath"]').classList.remove('hidden');
        } else if (action === 'message') {
            document.getElementById('message').classList.remove('hidden');
            document.querySelector('label[for="message"]').classList.remove('hidden');
        }
    }

    // Initialize form fields based on the default action
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        const action = urlParams.get('action') || 'print'; // Default to 'print' if not set
        document.getElementById('action').value = action;
        updateFormFields(); // Set the initial state of the form fields
        document.getElementById('action').addEventListener('change', handleActionChange);
    };
</script>
      

</body>
</html>
