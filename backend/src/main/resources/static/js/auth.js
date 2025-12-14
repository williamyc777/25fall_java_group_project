/**
 * 认证工具函数
 * 用于在所有页面中统一处理token的读取和验证
 */

/**
 * 从Cookie中获取token
 */
function getTokenFromCookie() {
    const cookies = document.cookie.split(';');
    for (let cookie of cookies) {
        const parts = cookie.trim().split('=');
        if (parts.length === 2 && parts[0] === 'token') {
            return decodeURIComponent(parts[1]);
        }
    }
    return null;
}

/**
 * 设置token到Cookie
 */
function setTokenToCookie(token) {
    // 设置Cookie，包含path、max-age和SameSite属性
    document.cookie = `token=${encodeURIComponent(token)}; path=/; max-age=604800; SameSite=Lax`;
    console.log('Token saved to cookie');
}

/**
 * 检查用户是否已登录
 */
function isLoggedIn() {
    const token = getTokenFromCookie();
    return token !== null && token.length > 0;
}

/**
 * 如果未登录，重定向到登录页
 */
function requireLogin() {
    if (!isLoggedIn()) {
        window.location.href = '/';
        return false;
    }
    return true;
}

/**
 * 获取token，如果不存在则重定向
 */
function getTokenOrRedirect() {
    const token = getTokenFromCookie();
    if (!token) {
        window.location.href = '/';
        return null;
    }
    return token;
}

/**
 * 获取token（便捷函数，等同于getTokenFromCookie）
 */
function getToken() {
    return getTokenFromCookie();
}

/**
 * 登出
 */
function logout() {
    // 清除Cookie
    document.cookie = 'token=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT';
    window.location.href = '/';
}


