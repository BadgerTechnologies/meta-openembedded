SUMMARY = "Python bindings for interacting with systemd over DBus"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4fbd65380cdd255951079008b364516c"

#SRC_URI[md5sum] = "f993739eca645749f18d4ccfd4a0fbf3"
#SRC_URI[sha256sum] = "cac2e42043ab28d43adf33dab493c6a3cf8a99794f824ae8af6d9cd6458b9972"

#SRC_URI = "https://files.pythonhosted.org/packages/source/p/pystemd/pystemd-0.13.2.tar.gz"
SRC_URI[md5sum] = "780fe0d823723085993bcc1680c31284"
SRC_URI[sha256sum] = "4dcfa4b13a55685c49d3d17c10631eca18c33770f66316f8ef2337b8951cc144"

DEPENDS = "systemd pkgconfig-native python3-wheel python3-setuptools python3-lxml"
RDEPENDS:${PN} += "python3-xml python3-pprint python3-pkgconfig python3-lxml"
REQUIRED_DISTRO_FEATURES = "systemd"

inherit pypi setuptools3 features_check

BBCLASSEXTEND = "native"

