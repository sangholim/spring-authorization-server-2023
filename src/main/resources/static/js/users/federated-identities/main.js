function deleteUserFederatedIdentities(userId) {
  const nodes = document.querySelectorAll("input[name='checkId']:checked");
  const ids = Array.from(nodes).map(x => x.value).join(",");
  const url = `${contextPath.value}console/users/${userId}/federated-identities?ids=${ids}`;
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