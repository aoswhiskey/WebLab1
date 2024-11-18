function sendAjax() {
    const xValue = document.querySelector('input[name="x-choose"]:checked').value;
    const yValue = truncateNumber(document.getElementById("y-input").value, 10);

    // Регулярные выражения для проверки значений
    const xRegExp = /^(-3|-2|-1|0|1|2|3|4|5)$/;
    const yRegExp = /^(-?[0-2](\.\d+)?|\+?[3-4](\.\d+)?)$/;
    const rRegExp = /^([12345])$/;

    const selectedROptions = document.querySelectorAll('input[name="r-choose"]:checked');

    // Проверяем, выбран ли X
    if (selectedROptions.length === 0) {
        showError();
        return;
    }

    selectedROptions.forEach(function (option) {
        let rValue = option.value;

        // Проверка значений X, Y, R
        if (xRegExp.test(xValue) && yRegExp.test(yValue) && rRegExp.test(rValue)) {
            hideError();
            // AJAX-запрос
            fetch("http://localhost:8080/calculate", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    x: parseInt(xValue),
                    y: parseFloat(yValue),
                    r: parseInt(rValue)
                })
            })
            .then(response => response.json())
            .then(function(data) {
                let result = data["result"] ? "Попадание" : "Промах";
                let time = data["time"];
            
                let newRow = 
                    `<tr>
                        <td>${xValue}</td>
                        <td>${parseFloat(yValue)}</td>
                        <td>${rValue}</td>
                        <td>${result}</td>
                        <td>${getCurrentDatetime()}</td>
                        <td>${time}</td>
                    </tr>`
                ;
            
                document.querySelector("#results tbody").insertAdjacentHTML('beforeend', newRow);
            })
            .catch(handleError);
        } else {
            showError();
        }
    });
}

// Отображение ошибки
function showError() {
    document.querySelector(".error_text").style.display = "block";
}

// Скрытие ошибки
function hideError() {
    document.querySelector(".error_text").style.display = "none";
}


// Обработка ошибки запроса
function handleError(error) {
    console.error("Ошибка:", error);
}

// Получение текущей даты и времени
function getCurrentDatetime() {
    const now = new Date();

    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');

    return `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;
}

// Обрезка числа до заданного количества десятичных знаков
function truncateNumber(num, decimalPlaces) {
    const numStr = num.toString();
    const dotIndex = numStr.indexOf('.');

    if (dotIndex === -1) {
        return numStr;
    }

    return numStr.slice(0, dotIndex + decimalPlaces + 1);
}