<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Access Denied</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8d7da;
            color: #721c24;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            text-align: center;
            border: 1px solid #f5c6cb;
            background-color: #f8d7da;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
        }
        h1 {
            margin-bottom: 10px;
        }
        p {
            margin: 5px 0;
        }
        a {
            text-decoration: none;
            color: #0056b3;
            font-weight: bold;
        }
    </style> 
</head>
<body>
    <div class="container">
        <h1>Access Denied</h1>
        <p>${errorMsg}</p>
        <p>You will be redirected to the <a href="${contextpath}/logout">login page</a> in a few seconds.</p>
    </div>
</body>
</html>