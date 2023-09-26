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
    "api": str(pathlib.Path(f"{script_path}/../../graphene-api/").resolve()),
    "server": str(pathlib.Path(f"{script_path}/../../graphene-server/").resolve()),
}
patch_dirs = {
    "api": str(pathlib.Path(f"{script_path}/../../patches/api/").resolve()),
    "server": str(pathlib.Path(f"{script_path}/../../patches/server/").resolve()),
}

def get_paths():
    paths = {}
    for type in ("server", "api"):
        type_path = f"{class_dir}/{type}/"
        real_paths = []
        patch_paths = []
        for i, (root, _, files) in enumerate(os.walk(type_path)):
            if os.path.exists(root):
                for file in files:
                    path = f"{root}/{file}"
                    if os.path.isfile(path):
                        real_paths.append(path)
                        patch_paths.append(path.replace(type_path, ""))
        paths[type] = {
            "real": real_paths,
            "patch": patch_paths
        }
    return paths

def create_strings(paths):
    patches = {}
    for type in ("API", "Server"):
        tlower = type.lower()
        patches[tlower] = {}
        os.chdir(source_dirs[tlower])
        patches[tlower] = "From 0000000000000000000000000000000000000000 Mon Sep 17 00:00:00 2001\n"
        patches[tlower] += "From: satvrn <pastawho@gmail.com>\n"
        patches[tlower] += f"Date: {datetime.now(tz=LOCAL_TZ).strftime('%a, %d %b %Y %H:%M:%S %z')}\n"
        patches[tlower] += f"Subject: [PATCH] Graphene {type} Classes\n\n\n"
        real_paths = paths[tlower]["real"]
        patch_paths = paths[tlower]["patch"]
        source_dir = source_dirs[tlower]
        for i, rp in enumerate(real_paths):
            patch_path = f"{source_dir}/src/main/java/{patch_paths[i]}"
            root_dir = f"{'/'.join(patch_path.split('/')[:-1])}"
            if not os.path.exists(root_dir):
                os.makedirs(root_dir)
            shutil.copy(rp, patch_path)
        subprocess.run(["git", "add", "."])
        result = subprocess.run(["git", "--no-pager", "diff", "--full-index", "--staged"], stdout=subprocess.PIPE, check=True).stdout.decode("utf-8")
        if result.strip() == "":
            patches[tlower] = ""
        else:
            patches[tlower] += result
    return patches

def create_patches(strings):
    for type in ("API", "Server"):
        tlower = type.lower()
        if strings[tlower] == "":
            continue
        os.chdir(patch_dirs[tlower])
        f = open(f"0002-Graphene-{type}-Classes.patch", "w")
        f.write(strings[tlower])
        f.close()

if __name__ == "__main__":
    create_patches(create_strings(get_paths()))
