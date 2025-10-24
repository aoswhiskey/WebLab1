function sendAjax() {
    const selectedXOptions = document.querySelectorAll('input[name="x-choose"]:checked');
    const yValue = truncateNumber(document.getElementById("y-input").value, 10);
    const rValue = document.querySelector('input[name="r-choose"]:checked').value;

    const xRegExp = /^(-2|-1\.5|-1|-0\.5|0|0\.5|1|1\.5|2)$/;
    const yRegExp = /^(-?[0-4](\.\d+)?)$/;
    const rRegExp = /^(1|1\.5|2|2\.5|3)$/;

    if (selectedXOptions.length === 0) {
        showError();
        return;
    }

    selectedXOptions.forEach(function(option) {
        let xValue = option.value;

        if (xRegExp.test(xValue) && yRegExp.test(yValue) && rRegExp.test(rValue)) {
            hideError();

            // Формируем URL с параметрами
            const url = `/calculate?x=${encodeURIComponent(xValue)}&y=${encodeURIComponent(yValue)}&r=${encodeURIComponent(rValue)}`;

            fetch(url, {method: "POST"})
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
                        </tr>`;
                    
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