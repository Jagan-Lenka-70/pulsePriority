#!/usr/bin/env sh

set -e

WRAPPER_PROPS=".mvn/wrapper/maven-wrapper.properties"
if [ ! -f "$WRAPPER_PROPS" ]; then
  echo "Missing $WRAPPER_PROPS"
  exit 1
fi

DIST_URL=$(grep '^distributionUrl=' "$WRAPPER_PROPS" | cut -d'=' -f2-)
if [ -z "$DIST_URL" ]; then
  echo "distributionUrl not found in $WRAPPER_PROPS"
  exit 1
fi

DIST_FILE=$(basename "$DIST_URL")
DIST_NAME=$(echo "$DIST_FILE" | sed 's/-bin\.zip$//')
TARGET_DIR="${HOME}/.m2/wrapper/dists/${DIST_NAME}"
ZIP_PATH="${TARGET_DIR}/${DIST_FILE}"

mkdir -p "$TARGET_DIR"

if ! ls "$TARGET_DIR"/apache-maven-* >/dev/null 2>&1; then
  echo "Downloading Maven distribution..."
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$DIST_URL" -o "$ZIP_PATH"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "$DIST_URL" -O "$ZIP_PATH"
  else
    echo "curl or wget is required."
    exit 1
  fi

  echo "Extracting Maven distribution..."
  if command -v unzip >/dev/null 2>&1; then
    unzip -oq "$ZIP_PATH" -d "$TARGET_DIR"
  else
    echo "unzip is required."
    exit 1
  fi
fi

MAVEN_HOME=$(ls -d "$TARGET_DIR"/apache-maven-* 2>/dev/null | head -n 1)
if [ -z "$MAVEN_HOME" ]; then
  echo "Could not locate extracted Maven directory."
  exit 1
fi

exec "$MAVEN_HOME/bin/mvn" "$@"
