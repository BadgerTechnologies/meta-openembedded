SUMMARY = "Colorful worry-free console applications for Linux, Mac OS X, and Windows."
HOMEPAGE = "https://github.com/matthewdeanmartin/colorclass"
LICENSE = "MIT"
SECTION = "devel/python"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1b2a533055839e54558a727657c1c73e"

inherit pypi python_poetry_core

SRC_URI += "file://0001-pyproject.toml-Use-poetry-core.patch"
SRC_URI[sha256sum] = "6d4fe287766166a98ca7bc6f6312daf04a0481b1eda43e7173484051c0ab4366"

PYPI_PACKAGE = "colorclass"

RDEPENDS:${PN} += "\
    python3-ctypes \
"

BBCLASSEXTEND = "native nativesdk"
