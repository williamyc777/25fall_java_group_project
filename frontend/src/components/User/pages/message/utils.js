function formatTime(str) {
    let times = str.split('T')
    return (times[0] + ' ' + times[1])
}

function nowDate(time) {
    let year = time.getFullYear();
    let month = (time.getMonth() + 1).toString().padStart(2, '0');
    let date = time.getDate().toString().padStart(2, '0');
    let hour = time.getHours().toString().padStart(2, '0');
    let minute = time.getMinutes().toString().padStart(2, '0');
    let second = time.getSeconds().toString().padStart(2, '0');
    return (
        year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second
    )
}

export { formatTime, nowDate }