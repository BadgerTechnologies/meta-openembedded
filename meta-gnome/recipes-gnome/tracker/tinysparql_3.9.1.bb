SUMMARY = "Tracker is a file search engine"
LICENSE = "GPL-2.0-only & LGPL-2.1-only"
LIC_FILES_CHKSUM = " \
    file://COPYING.GPL;md5=ee31012bf90e7b8c108c69f197f3e3a4 \
    file://COPYING.LGPL;md5=2d5025d4aa3495befef8f17206a5b0a1 \
"

DEPENDS = " \
    dbus-native \
    glib-2.0 \
    sqlite3 \
    dbus \
    icu \
    json-glib \
    libsoup \
    libstemmer \
"

inherit gettext gnomebase gobject-introspection vala gi-docgen bash-completion

SRC_URI += "file://0001-fix-reproducibility.patch"
SRC_URI += "file://0001-src-libtracker-sparql-meson.build-dont-create-compat.patch"
SRC_URI[archive.sha256sum] = "98e8933b946d8c54a77eadabd7f1d14cddd88fa43eef0d0ace3c0fcce646722b"

# text search is not an option anymore and requires sqlite3 build with
# PACKAGECONFIG[fts5] set (default)

# set required cross property sqlite3_has_fts5
do_write_config[vardeps] += "PACKAGECONFIG"
do_write_config:append() {
    echo "[properties]" > ${WORKDIR}/meson-tracker.cross
    echo "sqlite3_has_fts5 = 'true'" >> ${WORKDIR}/meson-tracker.cross
}

EXTRA_OEMESON = " \
    --cross-file ${WORKDIR}/meson-tracker.cross \
    -Dman=false \
    -Dsystemd_user_services=${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)} \
    -Dsystemd_user_services_dir=${systemd_user_unitdir} \
    -Dtests=false \
    ${@bb.utils.contains('GI_DATA_ENABLED', 'True', '-Dvapi=enabled', '-Dvapi=disabled', d)} \
"

GIR_MESON_ENABLE_FLAG = 'enabled'
GIR_MESON_DISABLE_FLAG = 'disabled'
GIDOCGEN_MESON_OPTION = "docs"

PACKAGECONFIG ??= " \
	${@bb.utils.contains("DISTRO_FEATURES", "zeroconf", "avahi", "", d)} \
	${@bb.utils.contains("DISTRO_FEATURES", "api-documentation", "docs", "", d)} \
"
PACKAGECONFIG[avahi] = "-Davahi=enabled,-Davahi=disabled,avahi,"
PACKAGECONFIG[docs] = ",,graphviz-native"

do_install:prepend() {
    sed -i -e 's|${B}|${TARGET_DBGSRC_DIR}|g' ${B}/src/libtinysparql/tracker-sparql-enum-types.c
    sed -i -e 's|${B}|${TARGET_DBGSRC_DIR}|g' ${B}/src/libtinysparql/core/tracker-data-enum-types.c
}

FILES:${PN} += " \
    ${datadir}/dbus-1 \
    ${libdir}/tinysparql-3.0 \
    ${systemd_user_unitdir} \
"
