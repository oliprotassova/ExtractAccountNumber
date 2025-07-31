require: slotfilling/slotFilling.sc
  module = sys.zb-common
  
patterns:
    $has_numbers = * (*1*|*2*|*3*|*4*|*5*|*6*|*7*|*8*|*9*|*0*) *
    
theme: /HasNumbers

    state: Start
        q!: $regex</start>
        a: Здравствуйте! Назовите номер счёта, о котором идет речь?

    state: ExtractAccountNumber
        q!: $has_numbers
        script:
            // Удаляем всё, что не цифры
            var cleanDigits = $request.query.replace(/\D+/g, '');
            
            // Проверяем сколько цифр всего в запросе - число должно быть кратным 9
            if (cleanDigits.length === 0 || cleanDigits.length % 9 !== 0) {
                $reactions.answer("Пожалуйста, укажите номер счета (ровно 9 цифр)");
                return;
            }
            
            // Делим полученные цифры на группы по 9 штук
            var validAccounts = [];
            for (var i = 0; i < cleanDigits.length; i += 9) {
                var account = cleanDigits.substr(i, 9);
                validAccounts.push(account);
            }
            
            // Выводим результат с получеными номерами счетов
            if (validAccounts.length > 0) {
                $reactions.answer("Найдены номера счетов: " + validAccounts.join(", "));
            }

    state: NoMatch
        event!: noMatch
        a: Пожалуйста, укажите номер счета. Он должен содержать 9 цифр.

