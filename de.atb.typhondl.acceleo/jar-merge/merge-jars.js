const fs = require('fs');
const path = require('path');
const archiver = require('archiver');
var zipper = require("zip-local");

function createJar(jarPath, path) {
	var output = fs.createWriteStream(jarPath);
	var archive = archiver('zip');
	output.on('close', function () { });
	archive.on('error', function(err) {
	    throw err;
	});
	archive.pipe(output);
	archive.directory(path + '/META-INF', 'META-INF');
	archive.directory(path + '/de', 'de');
	archive.finalize();
}

function rmdir(dir) {
    var list = fs.readdirSync(dir);
    for(var i = 0; i < list.length; i++) {
        var filename = path.join(dir, list[i]);
        var stat = fs.statSync(filename);

        if(filename == "." || filename == "..") {
            // pass these files
        } else if(stat.isDirectory()) {
            // rmdir recursively
            rmdir(filename);
        } else {
            // rm fiilename
            fs.unlinkSync(filename);
        }
    }
    fs.rmdirSync(dir);
}

function merge() {
	const pluginJar = "../target/de.atb.typhondl.acceleo-1.0.0-SNAPSHOT.jar";
	const acceleoJar = "../bin/lib/de.atb.typhondl.acceleo-1.0.0.jar";

	const tmpDir = "__temp";
	if (fs.existsSync(tmpDir)) {
		rmdir(tmpDir);
	}
	fs.mkdirSync(tmpDir);

	const tempPlugin = tmpDir + "/plugin";
	const tempAccel = tmpDir + "/accel";
	fs.mkdirSync(tempPlugin);
	fs.mkdirSync(tempAccel);

	zipper.sync.unzip(pluginJar).save(tempPlugin);
	zipper.sync.unzip(acceleoJar).save(tempAccel);

	const genPath = "de/atb/typhondl/acceleo";
	const mtlFIles = ["common/utilityGenerator", "files/generateDeployment", "main/generate"];
	
	for (let j = 0; j < mtlFIles.length; j++) {
		fs.copyFileSync(path.join(tempAccel, genPath, mtlFIles[j] + ".emtl"), path.join(tempPlugin, genPath, mtlFIles[j] + ".emtl"));
	}

	const mergedJar = "de.atb.typhondl.acceleo-1.0.0-SNAPSHOT.jar";
	if (fs.existsSync(mergedJar)) {
		fs.unlinkSync(mergedJar);
	}

	createJar(mergedJar, tempPlugin);

	//rmdir(tmpDir);
}

merge();