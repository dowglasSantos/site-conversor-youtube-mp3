async function baixar() {
    const link = document.getElementById("link").value;
    document.getElementById("status").innerText = "Baixando...";

    try {
        const res = await fetch("/api/baixar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ link })
    });

        const text = await res.text();
        document.getElementById("status").innerText = text;
    } catch (err) {
        document.getElementById("status").innerText = "Erro: " + err.message;
    }
}