const nav = {
    active: function() {
        const nodes = document.querySelectorAll("a[class='nav-link']");
        if(nodes.length == 0) {
            return
        }
        var elements = Array.from(nodes);
        elements.filter( x => x .href== location.href).forEach(x => x.classList.add("active"));
    }
}