const authorization = {
    table: {
        columns: ["id", "registeredClientId", "principalName", "authorizationGrantType", "authorizedScopes"	,"authorizationCodeIssuedAt", "authorizationCodeExpiresAt",	"accessTokenIssuedAt",	"accessTokenExpiresAt",	"accessTokenType",	"accessTokenScopes",	"oidcIdTokenIssuedAt",	"oidcIdTokenExpiresAt",	"refreshTokenIssuedAt",	"refreshTokenExpiresAt"],
        create: function(elements) {
            this.head.create();
            this.body.create(elements);
        }
    }
}

authorization.table.head = {
    node: function() {
        return document.querySelector("table").tHead;
    },
    create: function() {
        var row = this.node().insertRow();
        authorization.table.columns.map ( column => {
            var cell = row.insertCell();
            var span = document.createElement('span');
            span.innerText = column
            if(column == "id") {
                span.style.width = "50px";
                span.innerText = "";
            }
            cell.appendChild(span);
        });
    }
}

authorization.table.body = {
    node: function() {
        return document.querySelector("table").tBodies[0];
    },
    create: function(elements) {
       authorizations.map(element => {
           this.row.create(this.node(), element);
       });
    },
    row: {
       create: function (body, element) {
           var row = body.insertRow();
           authorization.table.columns.map ( column => {
               var cell = row.insertCell();
               const value = (element[column] !== undefined)? element[column] : "";
               var node = this.span(value);
               if(column == "id") {
                   node = this.checkBox(value)
               }
               cell.appendChild(node);
           });
       },
       span: function(text) {
           var node = document.createElement('span');
           node.classList.add("d-block")
           node.classList.add("text-truncate")
           node.innerText = text;
           return node
       },
       checkBox: function(value) {
           var node = document.createElement('input');
           node.name = "checkId";
           node.type = "checkbox";
           node.value = value;
           node.classList.add("form-check-input")
           return node;
       }
    }
}