var fs = require('fs');

var file_content = fs.readFileSync("E:\\cocos\\projs\\sango_card\\assets\\resources\\data\\Enhanceconfig.json",'utf8');
var jsonObj = JSON.parse(file_content);

console.log("size:"+jsonObj.levels.length);

var sum_gamemoney = 0;
for(var i=0;i<46;i++){
    var a_level = jsonObj.levels[i];
    sum_gamemoney += a_level.gamemoney_coef;
}

console.log("sum:"+sum_gamemoney);