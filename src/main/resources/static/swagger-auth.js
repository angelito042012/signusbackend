document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("jwt");
    if (token) {
        const swaggerUI = document.querySelector("iframe");
        if (swaggerUI) {
            swaggerUI.src = "/api/swagger-ui/index.html";
            swaggerUI.onload = () => {
                const iframeDoc = swaggerUI.contentDocument || swaggerUI.contentWindow.document;
                iframeDoc.querySelector("header").setRequestHeader("Authorization", `Bearer ${token}`);
            };
        }
    } else {
        alert("No estás autenticado. Por favor, inicia sesión.");
        window.location.href = "/admin-login.html";
    }
});