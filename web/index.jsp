<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Informasi Nilai Mahasiswa</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<div id="header">
    <h2>Informasi Nilai Mahasiswa</h2>
    <h1>UNIVERSITAS PAMULANG</h1>
    <p>JL. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p>
</div>
<div style="text-align:center; margin-top:50px;">
    <h3>Login</h3>
    <%
        String pesan = (String) session.getAttribute("pesan");
        if (pesan != null) {
            out.println("<p style='color:red'>" + pesan + "</p>");
            session.removeAttribute("pesan");
        }
    %>
    <form method="post" action="LoginController">
        <table style="margin:auto;">
            <tr>
                <td>Username</td>
                <td><input type="text" name="username"></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><input type="password" name="password"></td>
            </tr>
            <tr>
                <td colspan="2" style="text-align:center;">
                    <input type="submit" value="Login">
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="footer">
    <p>Copyright &copy; 2014 Universitas Pamulang</p>
    <p>Jl. Surya Kencana No. 1 Pamulang, Tangerang Selatan, Banten</p>
</div>
</body>
</html>