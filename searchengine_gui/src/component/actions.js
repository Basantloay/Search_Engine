export function ShowSuggestions(){
    
    let text1 = document.getElementById('myinput');
    let suggestions=document.getElementsByClassName('list-group')[0];
    const isEmpty = str => !str.trim().length;
    text1.addEventListener('input',function(){
    if (isEmpty(this.value)) {
        suggestions.classList.add('none');
    } else {
        suggestions.classList.remove('none');
    }
});
}