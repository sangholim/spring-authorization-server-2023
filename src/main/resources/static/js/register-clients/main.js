function deleteClients() {
  const nodes = document.querySelectorAll("input[name='checkId']:checked");
  const ids = Array.from(nodes).map(x => x.value).join(",");
  const url = "/console/register-clients?ids=" + ids;
  const csrf = document.querySelector("input[name='_csrf']").value;
  var xhr = new XMLHttpRequest();
  xhr.open("DELETE",url);
  xhr.onreadystatechange = function() { // listen for state changes
    if (xhr.readyState == 4) { // when completed we can move away
        window.location.reload(true);
    }
  }
  xhr.setRequestHeader("x-csrf-token", csrf);
  xhr.send();
}