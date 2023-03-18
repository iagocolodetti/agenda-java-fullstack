<div class="form-group" ng-app="app" ng-controller="controller" ng-init="init()">
    <h4 class="d-flex mb-3 text-muted">{{ contact.id === null ? 'Novo Contato' : 'Atualizar Contato' }}</h4>
    <form class="card" ng-submit="post()">
        <div class="card-body">
            <label class="form-group has-float-label mb-4">
                <input type="text" class="form-control" ng-model="contact.name" placeholder="Nome" />
                <span>Nome</span>
            </label>
            <label class="form-group has-float-label mb-4">
                <input type="text" class="form-control" ng-model="contact.alias" placeholder="Apelido" />
                <span>Apelido</span>
            </label>
            <h6 class="card-subtitle mb-1 text-muted" ng-show="contact.phone.length > 0">Telefone(s)</h6>
            <ul class="list-group text-center mb-2" ng-show="contact.phone.length > 0">
                <li class="list-group-item d-flex justify-content-between align-items-center" ng-repeat="p in contact.phone" ng-show="p.deleted === false">
                    {{ p.phone }}
                    <button type="button" class="btn btn-outline-danger btn-sm" ng-click="deletePhone(p)">
                        <span class="fa fa-trash" />
                    </button>
                </li>
            </ul>
            <div class="form-group input-group mb-4">
                <label class="has-float-label">
                    <input type="text" class="form-control" ng-model="phone" placeholder="Telefone" />
                    <span>Telefone</span>
                </label>
                <div class="input-group-append">
                    <button type="button" class="btn btn-outline-primary" ng-click="addPhone()">
                        <span class="fa fa-plus" />
                    </button>
                </div>
            </div>
            <h6 class="card-subtitle mb-1 text-muted" ng-show="contact.email.length > 0">E-mail(s)</h6>
            <ul class="list-group text-center mb-2" ng-show="contact.email.length > 0">
                <li class="list-group-item d-flex justify-content-between align-items-center" ng-repeat="e in contact.email" ng-show="e.deleted === false">
                    {{ e.email }}
                    <button type="button" class="btn btn-outline-danger btn-sm" ng-click="deleteEmail(e)">
                        <span class="fa fa-trash" />
                    </button>
                </li>
            </ul>
            <div class="form-group input-group mb-4">
                <label class="has-float-label">
                    <input type="email" class="form-control" ng-model="email" placeholder="Email" />
                    <span>E-mail</span>
                </label>
                <div class="input-group-append">
                    <button type="button" class="btn btn-outline-primary" ng-click="addEmail()">
                        <span class="fa fa-plus" />
                    </button>
                </div>
            </div>
            <div class="d-flex justify-content-center mt-4 mb-2" ng-show="error.length > 0">
                <span class="alert alert-danger alert-fix text-center" role="alert">
                    {{ error }}
                </span>
            </div>
            <div class="d-flex justify-content-between mb-1">
                <button type="button" class="btn btn-secondary btn-fix" ng-click="handleClearOrCancel()" ng-disabled="{{ disabled }}">{{ contact.id === null ? 'Limpar' : 'Cancelar' }}</button>
                <input type="submit" class="btn btn-primary btn-fix" value="Salvar" ng-disabled="{{ disabled }}" />
            </div>
        </div>
    </form>
</div>
