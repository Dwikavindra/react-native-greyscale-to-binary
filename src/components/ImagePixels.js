/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable prettier/prettier */
/* eslint-disable no-undef */
/* eslint-disable no-dupe-keys */
import {View, Text, Image, FlatList, StyleSheet, Dimensions, TouchableOpacity} from 'react-native';
import React, {useEffect, useState} from 'react';
import PixelsImage from '../../PixelsImage';
import ImagePicker from 'react-native-image-crop-picker';
import RNFS from 'react-native-fs';

const start_width = Dimensions.get('screen').width;
const start_height = Dimensions.get('screen').height;

const ImagePixels = () => {
  const [image, setImage] = useState(null);
  const [images, setImages] = useState(null);

  //Getting file paths
  const [downloadsFolder, setDownloadsFolder] = useState('');
  const [documentsFolder, setDocumentsFolder] = useState('');
  const [externalDirectory, setExternalDirectory] = useState('');

  //get user's file paths from react-native-fs
  useEffect(() => {
    setDownloadsFolder(RNFS.DownloadDirectoryPath);
    setDocumentsFolder(RNFS.DocumentDirectoryPath); //alternative to MainBundleDirectory.
    setExternalDirectory(RNFS.ExternalStorageDirectoryPath);
  }, []);

  useEffect(() => {
    getFileContent(RNFS.DownloadDirectoryPath + '/assets'); //run the function on the first render.
  }, []);

  //this component will render our list item to the UI
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
      <View style={{
        padding: 10,
      }}>
        <Text style={styles.title}>{index}</Text>
        {/* The isFile method indicates whether the scanned content is a file or a folder*/}
        <Item name={item.name} isFile={item.isFile()} />
      </View>
    );
  };

  //Reading directories
  const [files, setFiles] = useState([]);
  const getFileContent = async (path) => {
    const reader = await RNFS.readDir(path);
    setFiles(reader);
  };

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
        let pathsDir = image.path.split(/\r?\n/);
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
    <View style={[styles.container, {
      flexDirection: 'column',
    }]}>
      <View style={{ flex: 2, backgroundColor: '#F2F3F4' }}>
      <View style={{ flexDirection: 'row', alignSelf: 'center', margin: 8 }}>
      <Image
        source={image}
        style={{
          width: 130,
          height: 130,
          borderRadius: 20,

        }}
        />
      <Image
        source={image}
        style={{
          width: 130,
          height: 130,
          borderRadius: 20,
        }}
        />
      </View>
      </View>
      <View style={{ flex: 2, backgroundColor: '#D7DBDD'}}>
        <View style={{ justifyContent: 'space-evenly', width: '50%', height: 40, alignSelf: 'center', flex: 1 }}>
        <View >
        <TouchableOpacity onPress={() => pickSingleWithCamera()} style={{backgroundColor: 'tomato', padding: 8, borderRadius: 12}}>
          <Text style={{
            fontSize: 12,
            textTransform: 'uppercase',
            textAlign: 'center',
          }}>
            take image
          </Text>
        </TouchableOpacity>
        </View>
        <View>
        <TouchableOpacity onPress={() => pickSingleBase64()} style={{backgroundColor: 'tomato', padding: 8, borderRadius: 12}}>
        <Text style={{
            fontSize: 12,
            textTransform: 'uppercase',
            textAlign: 'center',
          }}>
            choose image
          </Text>
        </TouchableOpacity>
        </View>
        </View>
      </View>
      <View style={{ flex: 3, backgroundColor: '#D0D3D4' }}>
        <FlatList
          data={files}
          renderItem={renderItem}
          keyExtractor={(item) => item.name}
        />
      </View>
    </View>
  );
};

export default ImagePixels;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    width: start_width,
    height: start_height,
  },
});
