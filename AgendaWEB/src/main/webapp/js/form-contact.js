const app = angular.module('app', []);

app.controller('controller', ($scope, $http, $location, $window) => {

    const baseUrl = $location.absUrl().replace('contacts', '');

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
        if ($scope.phone.length > 0) {
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
        if ($scope.email.length > 0) {
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
        } else if ($scope.contact.alias.length === 0) {
            $scope.error = 'Erro: Preencha o campo destinado ao apelido.';
        } else if ($scope.contact.phone.length === 0 || !$scope.contact.phone.some(p => !p.deleted)) {
            $scope.error = 'Erro: Você deve adicionar ao menos um telefone.';
        } else if ($scope.contact.email.length === 0 || !$scope.contact.email.some(e => !e.deleted)) {
            $scope.error = 'Erro: Você deve adicionar ao menos um e-mail.';
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
