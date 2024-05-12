document.addEventListener("DOMContentLoaded",mainrender);

var mainUSER ={
    logged: false,
    user : {id:"", rol:""}
}

async function mainrender(){
    try{
        await renderheader();
        await renderfoot();
    } catch(error){
        return;
    }
}

function renderheader(){
    html=`
            <div>
                <div>
                    <p>Facturación </p> <p>electronica</p></div>
                <div><img class="logo" src="../../../static/Images/logo.png" alt=""/></div>
            </div>

            <ul class="Menu">
                <li>
                    <a href="/Presentation/AcercaDe/view.html">Acerca De</a>
                    <a href="templates/Presentation/AcercaDe/view.html">Acerca De</a>
                </li>
                <li>
                    <a href="/presentation/Login/show">Login</a>
                </li>
            </ul>
        `;
        document.querySelector('#header').innerHTML = html;
}
function renderfoot(){
    html=`
         <footer>
            facturasOnline@best.team.com
        </footer>
        `;
    document.querySelector('#footer').innerHTML = html;
}