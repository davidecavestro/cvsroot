#!/bin/bash
#Autogenera i file derivati
LICENSE_DIR_PATH="license-contents/license"

html2text -style pretty -o $LICENSE_DIR_PATH/LICENSE.txt $LICENSE_DIR_PATH/license.html

