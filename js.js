window.onload = function(){
    var edit = document.getElementById('edittext');
    var result = document.getElementById('result');
    var orignal = document.getElementById('orignal');
    var preview = document.getElementById('preview');
    var copy = document.getElementById('copy');
    var rotation = 0;
    var ori = '';
    var resultText = '';
    var newString = '';
    var neworiString = '';
    edit.addEventListener('input',function(){
        convert(edit.value);
    });
    copy.addEventListener('click',function(){
        copyTextToClipboard(newString);
    });
    function fallbackCopyTextToClipboard(text) {
        var textArea = document.createElement("textarea");
        textArea.value = text;
        
        // Avoid scrolling to bottom
        textArea.style.top = "0";
        textArea.style.left = "0";
        textArea.style.position = "fixed";
      
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
      
        try {
          var successful = document.execCommand('copy');
          var msg = successful ? 'successful' : 'unsuccessful';
          console.log('Fallback: Copying text command was ' + msg);
        } catch (err) {
          console.error('Fallback: Oops, unable to copy', err);
        }
      
        document.body.removeChild(textArea);
      }
      function copyTextToClipboard(text) {
        if (!navigator.clipboard) {
          fallbackCopyTextToClipboard(text);
          return;
        }
        navigator.clipboard.writeText(text).then(function() {
          console.log('Async: Copying to clipboard was successful!');
        }, function(err) {
          console.error('Async: Could not copy text: ', err);
        });
      }
    var letters = "aɐbqcɔdpeǝfɟgƃhɥiıjɾkʞlןmɯnuoopdqbrɹsstʇunvʌwʍxxyʎzzA∀CƆD◖EƎFℲG⅁HHIIJſK⋊L˥MWNNOOPԀQΌRᴚSST⊥U∩VΛWMXXY⅄ZZ&⅋.˙,'[]][())({}}{?¿!¡',\"„<>><_‾\"„\\/`,‿⁀⁅⁆∴∵001Ɩ2ᄅ3Ɛ4ㄣ5ϛ697ㄥ8896" ;
        var convertedMap = {};
        var i = 0;
        for(i = i;i<letters.length - 1;){
            convertedMap[letters.charAt(i)] = letters.charAt(i+1);
            convertedMap[letters.charAt(i+1)] = letters.charAt(i);
            i=i+2
        }
        convertedMap['B'] = 'q';
        function convert(old){
            newString = '';
            for(var i = old.length - 1;i>=0;i--){
                if(old.charAt(i) in convertedMap){
                    newString+=convertedMap[old.charAt(i)];
                }else{
                    newString+=old.charAt(i);
                }
            }  
            neworiString = edit.value;
            result.innerHTML = newString;  
            orignal.innerHTML = 'ORIGNAL:'+'<br>'+neworiString;
        }
    $(function(){
        $('#preview').on('click',function(){
            rotation=rotation+180;
            if(rotation===360){
                rotation=0;
            }
            ori = edit.value;
            $('#result').css({'transform' : 'rotate('+ rotation +'deg)'});
            $('#orignal').toggle();
            $('#orignal').html('ORIGNAL:'+'<br>'+ori);
        });
    });
}