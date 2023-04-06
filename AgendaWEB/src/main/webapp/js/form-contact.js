const app = angular.module('app', []);

app.controller('controller', ($scope, $http, $location, $window) => {

    const baseUrl = $location.absUrl().replace('contacts', '');
    
    const NameValidation = {
        minLength: 3,
        maxLength: 45
    };
    
    const AliasValidation = {
        minLength: 3,
        maxLength: 45
    };
    
    const phoneValidation = {
        minLength: 7,
        maxLength: 20
    };
    
    const emailValidation = {
        pattern: /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/,
        nameMaxLength: 64,
        addressMaxLength: 190
    };
    
    function isValidPhone(text) {
        return text.length >= phoneValidation.minLength && text.length <= phoneValidation.maxLength;
    }
    
    function isValidEmail(text) {
        const _email = text.split('@');
        return emailValidation.pattern.test(text) && _email[0].length <= emailValidation.nameMaxLength && _email[1].length <= emailValidation.addressMaxLength;
    }

    $scope.contact = {
        id: null,
        name: '',
        alias: '',
        phone: [],
        email: []
    };

    var lastPhoneID = 0;
    var lastEmailID = 0;

    $scope.error = '';

    $scope.disabled = false;

    $scope.init = async () => {
        try {
            const response = await $http.get(`${baseUrl}/contact`);
            if (response.data) {
                $scope.contact = response.data;
            }
        } finally {
            $scope.$apply();
        }
    };

    $scope.addPhone = () => {
        if (isValidPhone($scope.phone)) {
            const {phone} = $scope.contact;
            $scope.contact.phone.push({
                id: --lastPhoneID,
                phone: $scope.phone,
                deleted: false
            });
            $scope.phone = '';
        }
    };

    $scope.deletePhone = (phone) => {
        var index = $scope.contact.phone.indexOf(phone);
        if ($scope.contact.phone[index].id < 0) {
            $scope.contact.phone.splice(index, 1);
        } else {
            $scope.contact.phone[index].deleted = true;
        }
    };

    $scope.addEmail = () => {
        if (isValidEmail($scope.email)) {
            const {email} = $scope.contact;
            $scope.contact.email.push({
                id: --lastEmailID,
                email: $scope.email,
                deleted: false
            });
            $scope.email = '';
        }
    };

    $scope.deleteEmail = (email) => {
        var index = $scope.contact.email.indexOf(email);
        if ($scope.contact.email[index].id < 0) {
            $scope.contact.email.splice(index, 1);
        } else {
            $scope.contact.email[index].deleted = true;
        }
    };

    $scope.handleClearOrCancel = () => {
        $scope.contact = {
            id: null,
            name: '',
            alias: '',
            phone: [],
            email: []
        };
        lastPhoneID = 0;
        lastEmailID = 0;
    };

    $scope.post = async () => {
        if ($scope.contact.name.length === 0) {
            $scope.error = 'Erro: Preencha o campo destinado ao nome.';
        } else if ($scope.contact.name.length < NameValidation.minLength || $scope.contact.name.length > NameValidation.maxLength) {
            $scope.error = `Erro: O nome deve possuir de ${NameValidation.minLength} à ${NameValidation.maxLength} caracteres.`;
        } else if ($scope.contact.alias.length === 0) {
            $scope.error = 'Erro: Preencha o campo destinado ao apelido.';
        } else if ($scope.contact.alias.length < AliasValidation.minLength || $scope.contact.alias.length > AliasValidation.maxLength) {
            $scope.error = `Erro: O apelido deve possuir de ${AliasValidation.minLength} à ${AliasValidation.maxLength} caracteres.`;
        } else if ($scope.contact.phone.length === 0 || !$scope.contact.phone.some(p => !p.deleted)) {
            $scope.error = 'Erro: Você deve adicionar ao menos um telefone.';
        } else if (!$scope.contact.phone.some(p => isValidPhone(p.phone))) {
            $scope.error = `Erro: Existe(m) telefone(s) inválido(s) na lista, o telefone deve possuir de ${phoneValidation.minLength} à ${phoneValidation.maxLength} dígitos.`;
        } else if ($scope.contact.email.length === 0 || !$scope.contact.email.some(e => !e.deleted)) {
            $scope.error = 'Erro: Você deve adicionar ao menos um e-mail.';
        } else if (!$scope.contact.email.some(e => isValidEmail(e.email))) {
            $scope.error = 'Erro: Existe(m) endereço(s) de e-mail inválido(s) na lista.';
        } else {
            try {
                $scope.disabled = true;
                await $http.post(`${baseUrl}/contact`, $scope.contact);
                window.location = self.location;
            } catch (error) {
                if (error.data.status === '401') {
                    $window.location.reload();
                } else {
                    $scope.disabled = false;
                    $scope.error = `Erro: ${error.data.error}.`;
                    $scope.$apply();
                }
            }
        }
    };
});
