/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable prettier/prettier */
/* eslint-disable no-undef */
/* eslint-disable no-dupe-keys */
import {View, Text, Image, FlatList, StyleSheet} from 'react-native';
import React, {useEffect, useState} from 'react';
import PixelsImage from '../../PixelsImage';
import ImagePicker from 'react-native-image-crop-picker';
import RNFS from 'react-native-fs';

const ImagePixels = () => {
  const [image, setImage] = useState(null);
  const [images, setImages] = useState(null);
  const [downloadsFolder, setDownloadsFolder] = useState('');
  const [documentsFolder, setDocumentsFolder] = useState();
  const [externalDirectory, setExternalDirectory] = useState('');
  const folderPath = RNFS.DownloadDirectoryPath + '/assets';
  const [files, setFiles] = useState([]);

  const getImageName = async () =>{
    console.log(typeof (image));
    console.log('first------111111----', image);
    console.log('first------IMAGE----', documentsFolder);
  };
  const getFileContent = async (path) => {
    const reader = await RNFS.readDir(path);
    setFiles(reader);
  };
  useEffect(() => {
    getFileContent(RNFS.DownloadDirectoryPath + '/assets'); //run the function on the first render.
    getImageName();
  }, []);

  const makeDirectory = async (folderPath) => {
    await RNFS.mkdir(folderPath); //create a new folder on folderPath
  };

  const Item = ({ name, isFile }) => {
    return (
      <View>
        <Text style={styles.name}>Name: {name}</Text>
        <Text> {isFile ? 'It is a file' : "It's a folder"}</Text>
      </View>
    );
  };

  const renderItem = ({ item, index }) => {
    return (
      <View>
        <Text style={styles.title}>{index}</Text>
        {/* The isFile method indicates whether the scanned content is a file or a folder*/}
        <Item name={item.name} isFile={item.isFile()} />
      </View>
    );
  };

  useEffect(() => {
    //get user's file paths from react-native-fs
    setDownloadsFolder(RNFS.DownloadDirectoryPath);
    const gg = RNFS.DocumentDirectoryPath + '/Pictures/';
    setDocumentsFolder(gg); //alternative to MainBundleDirectory.
    setExternalDirectory(RNFS.ExternalStorageDirectoryPath);
  }, []);

  useEffect(() => {
    makeDirectory(folderPath); //execute this function on first mount
    const readDirectory = RNFS.DownloadDirectoryPath + '/assets';
    getFileContent(readDirectory); //this function was defined in the previous example
  }, []);

  const pickSingleWithCamera = (cropping, mediaType = 'photo') => {
    ImagePicker.openCamera({
      cropping: cropping,
      width: 500,
      height: 500,
      includeExif: true,
      mediaType,
    })
      .then(image => {
        let pathsDir = image.path.split(/\r?\n/);
        console.log('received base64 image', pathsDir);
        setImage({
          uri: image.path,
          width: image.width,
          height: image.height,
          mime: image.mime,
          mime: image.data,
        });
        setImages(null);
        PixelsImage.createBinaryPixels(image.path);
      })
      .catch(e => alert(e));
  };

  const pickSingleBase64 = cropit => {
    ImagePicker.openPicker({
      width: 300,
      height: 300,
      cropping: cropit,
      includeBase64: true,
      includeExif: true,
    })
      .then(image => {
        let pathsDir = image.uri.split(/\r?\n/);
        console.log('received base64 image', pathsDir);
        setImage({
          uri: `data:${image.mime};base64,` + image.data,
          width: image.width,
          height: image.height,
        });
        setImages(null);
        PixelsImage.createBinaryPixels(image.path);
      })
      .catch(e => alert(e));
  };

  const renderAsset = image => {
    return renderImage(image);
  };

  function testFunction() {
    PixelsImage.createBinaryPixels(image.path);
    console.log('goooooooooodddddddd');
  }

  return (
    <View style={{
      alignSelf: 'center',
      justifyContent: 'center',
      flexDirection: 'column',
      }}>
      <View style={{
        flex: 3,

      }}>
      <View style={{
          marginBottom: 20,
          justifyContent: 'center',
          alignItems: 'center',
      }}>
        <Image
        source={image}
        style={{
          width: 150,
          height: 150,
          borderRadius: 20,
        }}
        />
      </View>

      <View style={{
        marginBottom: 10,
      }}>
        <Text style={{
          fontSize: 20,
          backgroundColor: 'tomato',
            padding: 10,
          borderRadius: 20,
            textAlign: 'center',
            textTransform: 'uppercase',
          }}
          onPress={() => pickSingleWithCamera()}>
          clique
        </Text>
      </View>
      <View>
        <Text style={{
          fontSize: 20,
          backgroundColor: 'tomato',
            padding: 10,
          borderRadius: 20,
          textAlign: 'center',
          textTransform: 'uppercase',
          }}
          onPress={() => pickSingleBase64()}>
          take image
        </Text>
      </View>
      </View>
      <View style={{
        flex: 1,
      }}>
      <FlatList
        data={files}
        renderItem={renderItem}
        keyExtractor={(item) => item.name}
      />
      </View>
      <View style={{
        flex: 2,
        paddingBottom: 10,
      }}>
      <Text>Downloads Folder: {downloadsFolder}</Text>
      <Text>Documents folder: {documentsFolder}</Text>
      <Text>External storage: {externalDirectory}</Text>
      </View>
    </View>
  );
};

export default ImagePixels;

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});
