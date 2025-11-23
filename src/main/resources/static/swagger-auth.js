document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("jwt");
    if (token) {
        const swaggerUI = document.querySelector("iframe");
        if (swaggerUI) {
            swaggerUI.src = "/api/swagger-ui/index.html";
            swaggerUI.onload = () => {
                // Aquí no puedes modificar los encabezados de un iframe directamente
                console.log("El token está disponible:", token);
            };
        }
    } else {
        alert("No estás autenticado. Por favor, inicia sesión.");
        window.location.href = "/admin-login.html";
    }
});