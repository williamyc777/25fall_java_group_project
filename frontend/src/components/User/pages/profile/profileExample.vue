<template>
    <div class="bg">
        <header>
            <a href="#" id="logo">Logo</a>
            <ul>
                <li><a href="#" class="active">Home</a></li>
                <li><a href="#">About</a></li>
                <li><a href="#">Work</a></li>
                <li><a href="#">Contact</a></li>
            </ul>
        </header>
        <section>
            <img src="@/components/User/pages/profile/images/stars.png" alt="" id="stars">
            <img src="@/components/User/pages/profile/images/moon.png" alt="" id="moon">
            <img src="@/components/User/pages/profile/images/mountains_behind.png" alt="" id="mountain_behind">
            <div id="text">Moon Light</div>
            <a href="#" id="btn">explore</a>
            <img src="@/components/User/pages/profile/images/mountains_front.png" alt="" id="mountain_front">
        </section>
        <div class="sec">
            <h2>This is title</h2>
            <p>
                <!-- 这里是你的长文本内容 -->
                12323149236012897102958713098735
            </p>
        </div>
    </div>
</template>

<script>
export default {
    mounted() {
        //获取到一些关键元素
        let stars = this.$el.querySelector('#stars');
        let moon = this.$el.querySelector('#moon');
        let mountain_behind = this.$el.querySelector('#mountain_behind');
        let text = this.$el.querySelector('#text');
        let btn = this.$el.querySelector('#btn');
        let mountain_front = this.$el.querySelector('#mountain_front');

        //给窗口添加鼠标滚动事件
        window.addEventListener('scroll', () => {
            let value = window.scrollY;
            // stars.style.left = value * 0.25 + 'px';
            moon.style.top = value * 1.2 + 'px';
            mountain_behind.style.top = value * 0.5 + 'px';
            text.style.marginRight = value * 4 + 'px';
            text.style.marginTop = value * 0.5 + 'px';
            btn.style.marginRight = value * 1.5 + 'px';
            btn.style.marginTop = value * 0.5 + 'px';
        });
    }
};
</script>


<style scoped>
bg {
    background-color: #000; /* 设置黑色背景 */
    color: #fff; /* 设置文本颜色为白色，以便于对比 */
}
*{
    padding: 0;
    margin: 0;
    box-sizing: border-box;
}
body{
    background: linear-gradient(#2b1055,#7597de);
    min-height: 100vh;

    overflow-x: hidden;
    scroll-behavior: smooth;
}
header{
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    padding: 30px 100px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    z-index: 100;
}
header #logo{
    color: #fff;
    text-decoration: none;
    font-size: 2em;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 2px;
}
header ul{
    display: flex;
    justify-content: center;
    align-items: center;
}
header ul li{
    list-style: none;
    margin-left: 20px;
}
header ul li a{
    text-decoration:none;
    color: #fff;
    padding: 6px 25px;
    border-radius: 20px;
}
header ul li a:hover,
header ul li a.active{
    background-color: #fff;
    color: #2b1065;
}
section{
    position: relative;
    width: 100%;
    height: 100vh;
    padding: 100px;
    display: flex;
    justify-content: center;
    align-items: center;
    overflow: hidden;
    background-color: #000000;
}
section::before{
    content: '';
    position: absolute;
    bottom: 0;
    height: 100px;
    width: 100%;
    background: linear-gradient(to top,#1c0522,transparent);
    z-index: 1000;

}
section img{
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    pointer-events: none;
}
section img#moon{
    mix-blend-mode: screen;/*设置背景融合方式*/
}
section img#mountain_front{
    z-index: 15;
}
section #text{
    position: absolute;
    color: #fff;
    font-size: 7.5vw;
    white-space: nowrap;
    z-index: 10;
    transform: translateY(100px);
    right: -350px;
}
section #btn{
    text-decoration: none;
    color: #2b1055;
    padding: 8px 30px;
    border-radius: 20px;
    background-color: #fff;
    font-size: 1.5em;
    z-index: 10;
    display: inline-block;
}
.sec{
    position: relative;
    padding: 100px;
    background-color: #1c0522;
    z-index: 200;
}
.sec h2{
    font-size: 3.5em;
    color: #fff;
    margin-bottom: 10px;
}
.sec p{
    color: #fff;
    font-size: 1.2em;
}
</style>
