var fs = require('fs');


var jsonFile;
if(process.argv.length >=3){
    jsonFile = process.argv[2];
}

var file_content = fs.readFileSync(jsonFile,'utf8');
var jsonObj = JSON.parse(file_content);
console.log("size:"+jsonObj.length);

//处理Stage.json
for(var i=0;i<jsonObj.length;i++){
    var stages = jsonObj[i].stage;
    console.log("stage size:"+stages.length);
    for(var j=0;j<stages.length;j++){
        var aStage = stages[j];
        delete aStage["attrs"];
    }
}

//处理EliteStages.json
// for(var i=0;i<jsonObj.length;i++){
//     var stages = jsonObj[i].stage;
//     console.log("stage size:"+stages.length);
//     for(var stageId in stages){
//         var aStage = stages[stageId];
//         delete aStage["attrs"];
//     }
// }

var json_str = JSON.stringify(jsonObj,null,2);
fs.writeFileSync(jsonFile,json_str);