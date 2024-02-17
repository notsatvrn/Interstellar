#!/usr/bin/env python3

# Script to automatically generate the classes patch files.

import subprocess
import pathlib
import shutil
import os
from zoneinfo import ZoneInfo
from datetime import datetime

LOCAL_TZ = ZoneInfo("localtime")

script_path = os.path.realpath(__file__)
class_dir = str(pathlib.Path(f"{script_path}/../../classes/").resolve())
source_dirs = {
    "API": str(pathlib.Path(f"{script_path}/../../Graphene-API/").resolve()),
    "Server": str(pathlib.Path(f"{script_path}/../../Graphene-Server/").resolve()),
}
patch_dirs = {
    "API": str(pathlib.Path(f"{script_path}/../../patches/api/").resolve()),
    "Server": str(pathlib.Path(f"{script_path}/../../patches/server/").resolve()),
}

if __name__ == "__main__":
    for type in ("API", "Server"):
        # get paths
        real_paths = []
        patch_paths = []

        type_path = f"{class_dir}/{type.lower()}/"
        for i, (root, _, files) in enumerate(os.walk(type_path)):
            if os.path.exists(root):
                for file in files:
                    path = f"{root}/{file}"
                    if os.path.isfile(path):
                        real_paths.append(path)
                        patch_paths.append(path.replace(type_path, ""))

        # create patch                        
        source_dir = source_dirs[type]
        os.chdir(source_dir)

        patch = "From 0000000000000000000000000000000000000000 Mon Sep 17 00:00:00 2001\n"
        patch += "From: satvrn <pastawho@gmail.com>\n"
        patch += f"Date: {datetime.now(tz=LOCAL_TZ).strftime('%a, %d %b %Y %H:%M:%S %z')}\n"
        patch += f"Subject: [PATCH] Graphene: {type} Classes\n\n\n"
        
        for i, rp in enumerate(real_paths):
            patch_path = f"{source_dir}/src/main/java/{patch_paths[i]}"
            root_dir = f"{'/'.join(patch_path.split('/')[:-1])}"
            if not os.path.exists(root_dir):
                os.makedirs(root_dir)
            shutil.copy(rp, patch_path)
        subprocess.run(["git", "add", "."])
        result = subprocess.run(["git", "--no-pager", "diff", "--full-index", "--staged"], stdout=subprocess.PIPE, check=True).stdout.decode("utf-8")

        if result.strip() == "":
            continue
        else:
            patch += result

        # write patch
        os.chdir(patch_dirs[type])
        f = open(f"0002-Graphene-{type}-Classes.patch", "w")
        f.write(patch)
        f.close()
